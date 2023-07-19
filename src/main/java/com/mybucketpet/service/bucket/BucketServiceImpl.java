package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.*;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import com.mybucketpet.repository.bucket.BucketRepository;
import com.mybucketpet.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
        Bucket bucket = new Bucket(bucketAdd.getBucketTitle(), bucketAdd.getBucketContents(),
                bucketAdd.getOpenYn(), bucketAdd.getRecommendYn());
        Bucket saveBucket = bucketRepository.saveBucket(bucket);
        // 썸네일 등록
        Thumbnail thumbnail = new Thumbnail(thumbnailOriginalName, thumbnailSaveFileName);
        bucketRepository.saveThumbnail(thumbnail, saveBucket.getBucketId());
        // 태그 등록
        bucketRepository.saveTag(bucketAdd.getTagList(), saveBucket.getBucketId());

        return saveBucket.getBucketId();
    }

    @Override
    public BucketInfo findById(Long bucketId) {
        // 버킷 조회 - 조회시 에러 발생하면 삭제된 버킷이라고 메시지를 담아 RuntimeException 발생
        Bucket findBucket = bucketRepository.findBucketById(bucketId).orElseThrow(() -> new RuntimeException("BucketAlreadyDeleted"));
        // 썸네일 조회
        Thumbnail findThumbnail = bucketRepository.findThumbnailByBucketId(findBucket).get();
        // 태그 조회 - 태그 Id 값만 보유
        List<Tag> findTagList = bucketRepository.findTagByBucketId(findBucket);
        // 태그 목록 - 태그 Id와 태그 명을 모두 보유
        List<Tag> tagList = new ArrayList<>();
        for (Tag tag : findTagList) {
            Tag findTag = bucketRepository.findTagNameById(tag);
            tagList.add(findTag);
        }

        BucketInfo bucketInfo = new BucketInfo(findBucket.getBucketId(), findBucket.getBucketTitle(),
                findBucket.getBucketContents(), findBucket.getOpenYn(), findBucket.getRecommendYn(),
                findBucket.getCertiCnt(), findBucket.getScrapCnt(), findBucket.getLookupCnt(),
                findBucket.getModifyDate(), findThumbnail.getThumbnailFilename(),
                findThumbnail.getThumbnailSavename(), tagList);
        log.debug("bucketInfo = {}", bucketInfo);

        return bucketInfo;
    }

    @Override
    public List<Tag> findAllTag() {
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
        Bucket bucket = new Bucket();
        bucket.setBucketId(bucketId);
        // 버킷과 관련된 썸네일을 조회하였는데 조회되지 않는 경우 이미썸네일이 삭제되었다는 메시지와 함께 RuntimeException 발생
        Thumbnail findThumbnail = bucketRepository.findThumbnailByBucketId(bucket)
                .orElseThrow(() -> new RuntimeException("ThumbnailAlreadyDeleted"));
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
        String recommendYn = updateBucketList.get("recommendYn");
        // 기존 추천 여부의 값을 반대로 변경
        String changeRecommendValue = recommendYn.equalsIgnoreCase("y") ? "n" : "y";
        // 버킷의 추천 여부 값 변경
        bucketRepository.updateBucketRecommend(bucketId, changeRecommendValue);
    }

    @Override
    @Transactional
    public void updateBucket(Long bucketId, BucketUpdate bucketUpdate, MultipartFile file) throws IOException {
        // 버킷 수정
        bucketRepository.updateBucket(bucketId, bucketUpdate);
        // 첨부파일 null 값 체크 -> null이면 첨부파일을 수정하지 않았으므로 첨부파일 수정 진행 x
        // null이 아니라면 첨부파일 수정 진행 - 기존 첨부파일은 삭제 처리, 변경된 첨부파일을 저장
        if (file != null) {
            Bucket bucket = new Bucket();
            bucket.setBucketId(bucketId);
            // 기존 첨부파일 정보 조회
            Thumbnail findThumbnail = bucketRepository.findThumbnailByBucketId(bucket).get();
            // 변경된 파일 저장 처리
            String thumbnailOriginalName = file.getOriginalFilename();
            String thumbnailSaveFileName = fileService.saveFile(file);
            // 기존 첨부파일은 삭제 처리
            String thumbnailDeleteFileName = findThumbnail.getThumbnailSavename();
            fileService.deleteFile(thumbnailDeleteFileName);
            // 첨부파일 수정
            Thumbnail updateThumbnail = new Thumbnail(thumbnailOriginalName, thumbnailSaveFileName);
            bucketRepository.updateThumbnail(bucketId, updateThumbnail);
        }
        // 태그 수정의 경우 2가지 케이스
        // 1. 새롭게 추가된 태그 목록이 존재하는 경우 태그 추가 진행
        // 2. 삭제된 태그 목록이 존재하는 경우 태그 삭제 진행
        List<Tag> insertTagList = bucketUpdate.getInsertTagList();
        if (insertTagList.size() > 0) {
            bucketRepository.saveTag(insertTagList, bucketId);
        }
        List<Tag> deleteTagList = bucketUpdate.getDeleteTagList();
        if (deleteTagList.size() > 0) {
            bucketRepository.deleteTagList(deleteTagList, bucketId);
        }

    }


}