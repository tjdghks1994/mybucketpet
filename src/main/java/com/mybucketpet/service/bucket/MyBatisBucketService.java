package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.BucketAdd;
import com.mybucketpet.controller.admin.BucketInfo;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import com.mybucketpet.repository.bucket.BucketRepository;
import com.mybucketpet.service.file.FileSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MyBatisBucketService implements BucketService {

    private final BucketRepository bucketRepository;
    private final FileSaveService fileSaveService;
    @Autowired
    public MyBatisBucketService(BucketRepository bucketRepository, FileSaveService fileSaveService) {
        this.bucketRepository = bucketRepository;
        this.fileSaveService = fileSaveService;
    }

    @Override
    @Transactional
    public Long save(BucketAdd bucketAdd, MultipartFile file) throws IOException {
        String thumbnailOriginalName = file.getOriginalFilename();
        // 파일 저장 처리
        String thumbnailSaveFileName = fileSaveService.saveFile(file);
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
        Bucket findBucket = bucketRepository.findBucketById(bucketId).orElseThrow(() -> new RuntimeException("DeleteBucket"));
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
        log.info("bucketInfo = {}", bucketInfo);

        return bucketInfo;
    }

    @Override
    public List<Tag> findAllTag() {
        return bucketRepository.findAllTag();
    }

}
