package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.dto.*;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.BucketThumbnail;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.repository.bucket.BucketRepository;
import com.mybucketpet.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final FileService fileService;
    @Autowired
    public BucketServiceImpl(BucketRepository bucketRepository, FileService fileService) {
        this.bucketRepository = bucketRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public Long save(BucketAdd bucketAdd, MultipartFile file) throws IOException {
        String thumbnailOriginalName = file.getOriginalFilename();
        // 파일 저장 처리
        String thumbnailSaveFileName = fileService.saveFile(file);
        log.debug("thumbnailOriginalName = {}", thumbnailOriginalName);
        log.debug("thumbnailSaveFileName = {}", thumbnailSaveFileName);
        // 버킷 등록
        Long saveBucketId = bucketRepository.saveBucket(bucketAdd);
        // 썸네일 등록
        bucketRepository.saveThumbnail(thumbnailOriginalName, thumbnailSaveFileName, saveBucketId);
        // 태그 등록
        bucketRepository.saveTag(saveBucketId, bucketAdd.getTagList());

        return saveBucketId;
    }

    @Override
    public BucketResponse findById(Long bucketId) {
        // 버킷 조회 - 조회시 에러 발생하면 삭제된 버킷이라고 메시지를 담아 RuntimeException 발생
        BucketInfo findBucket = bucketRepository.findBucketById(bucketId);
        // 썸네일 조회
        ThumbnailInfo findThumbnail = bucketRepository.findThumbnailByBucketId(bucketId);
        // 태그 목록 조회
        List<TagInfo> findTagList = bucketRepository.findTagByBucketId(bucketId);

        return BucketResponse.builder()
                .bucketInfo(findBucket)
                .thumbnailInfo(findThumbnail)
                .tagList(findTagList)
                .build();
    }

    @Override
    public List<TagInfo> findAllTag() {
        return bucketRepository.findAllTag();
    }

    @Override
    public int getTotalBucketCount() {
        return bucketRepository.getTotalBucketCount();
    }
    @Override
    public List<BucketSearchResult> findAllBucket(BucketSearch bucketSearch, PageMakeVO pageMakeVO) {
        return bucketRepository.findAllBucket(bucketSearch, pageMakeVO);
    }

    @Override
    @Transactional
    public void deleteBucket(Long bucketId) {
        // 썸네일 테이블에서 튜플 삭제 전 파일이 저장된 정보를 가져오기 위해 썸네일 데이터 조회
        // 버킷과 관련된 썸네일을 조회하였는데 조회되지 않는 경우 이미썸네일이 삭제되었다는 메시지와 함께 RuntimeException 발생
        ThumbnailInfo findThumbnail = bucketRepository.findThumbnailByBucketId(bucketId);
        // 썸네일 테이블에서 삭제한 버킷 Id를 포함하고 있는 튜플 삭제 처리
        bucketRepository.deleteThumbnail(bucketId);
        // 태그 테이블에서 삭제한 버킷 Id를 포함하고 있는 튜플 삭제 처리
        bucketRepository.deleteTag(bucketId);
        // 버킷 테이블에서 bucketId를 포함하고 있는 튜플 삭제 처리
        bucketRepository.deleteBucket(bucketId);
        // 데이터가 전부 삭제된 후, 이미지 파일이 저장된 경로의 썸네일 이미지 파일 삭제 처리
        fileService.deleteFile(findThumbnail.getThumbnailSavename());
    }

    @Override
    public void updateBucketRecommend(Map<String, String> updateBucketList) {
        log.debug("updateBucketList = {}", updateBucketList);

        Long bucketId = Long.parseLong(updateBucketList.get("bucketId"));
        String currentRecommendValue = updateBucketList.get("recommendYn");
        // 클라이언트에서 전달된 변경할 추천 값
        String changeRecommendValue = updateBucketList.get("changeRecommendYn");
        // 현재 추천 값과 변경할 추천 값이 같지 않은 경우에만 update
        if (!currentRecommendValue.equals(changeRecommendValue)) {
            // 버킷의 추천 여부 값 변경
            bucketRepository.updateBucketRecommend(bucketId, changeRecommendValue);
        }
    }

    @Override
    @Transactional
    public void updateBucket(Long bucketId, BucketUpdate bucketUpdate, MultipartFile file) throws IOException {
        // 버킷 수정
        bucketRepository.updateBucket(bucketId, bucketUpdate);
        // 첨부파일 null 값 체크 -> null이면 첨부파일을 수정하지 않았으므로 첨부파일 수정 진행 x
        // null이 아니라면 첨부파일 수정 진행 - 기존 첨부파일은 삭제 처리, 변경된 첨부파일을 저장
        if (file != null) {
            // 기존 첨부파일 정보 조회
            ThumbnailInfo findThumbnail = bucketRepository.findThumbnailByBucketId(bucketId);
            // 변경된 파일 저장 처리
            String thumbnailOriginalName = file.getOriginalFilename();
            String thumbnailSaveFileName = fileService.saveFile(file);
            // 기존 첨부파일은 삭제 처리
            String thumbnailDeleteFileName = findThumbnail.getThumbnailSavename();
            fileService.deleteFile(thumbnailDeleteFileName);
            // 첨부파일 수정
            bucketRepository.updateThumbnail(bucketId, thumbnailOriginalName, thumbnailSaveFileName);
        }
        // 태그 수정의 경우 2가지 케이스
        // 1. 새롭게 추가된 태그 목록이 존재하는 경우 태그 추가 진행
        // 2. 삭제된 태그 목록이 존재하는 경우 태그 삭제 진행
        List<String> insertTagList = bucketUpdate.getInsertTagList();
        if (insertTagList.size() > 0) {
            bucketRepository.saveTag(bucketId, insertTagList);
        }
        List<String> deleteTagList = bucketUpdate.getDeleteTagList();
        if (deleteTagList.size() > 0) {
            bucketRepository.deleteTagList(bucketId, deleteTagList);
        }

    }


}
