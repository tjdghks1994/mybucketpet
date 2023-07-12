package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.BucketAdd;
import com.mybucketpet.controller.admin.BucketInfo;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import com.mybucketpet.repository.bucket.BucketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MyBatisBucketService implements BucketService {
    private final BucketRepository repository;
    @Autowired
    public MyBatisBucketService(BucketRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Long save(BucketAdd bucketAdd) {
        log.info("BucketAdd = {}", bucketAdd);
        // 버킷 등록
        Bucket bucket = new Bucket(bucketAdd.getBucketTitle(), bucketAdd.getBucketContents(),
                bucketAdd.getOpenYn(), bucketAdd.getRecommendYn());
        Bucket saveBucket = repository.saveBucket(bucket);
        // 썸네일 등록
        Thumbnail thumbnail = new Thumbnail(bucketAdd.getThumbnailFilename(), bucketAdd.getThumbnailSavename());
        repository.saveThumbnail(thumbnail, saveBucket.getBucketId());
        // 태그 등록
        repository.saveTag(bucketAdd.getTagList(), saveBucket.getBucketId());

        return saveBucket.getBucketId();
    }

    @Override
    public BucketInfo findById(Long bucketId) {
        // 버킷 조회 - 조회시 에러 발생하면 삭제된 버킷이라고 메시지를 담아 RuntimeException 발생
        Bucket findBucket = repository.findBucketById(bucketId).orElseThrow(() -> new RuntimeException("DeleteBucket"));
        // 썸네일 조회
        Thumbnail findThumbnail = repository.findThumbnailByBucketId(findBucket).get();
        // 태그 조회 - 태그 Id 값만 보유
        List<Tag> findTagList = repository.findTagByBucketId(findBucket);
        // 태그 목록 - 태그 Id와 태그 명을 모두 보유
        List<Tag> tagList = new ArrayList<>();
        for (Tag tag : findTagList) {
            Tag findTag = repository.findTagNameById(tag);
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

}
