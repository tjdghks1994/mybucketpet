package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.BucketSearch;
import com.mybucketpet.controller.admin.BucketSearchResult;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MybatisBucketRepository implements BucketRepository {

    private final BucketMapper bucketMapper;
    @Autowired
    public MybatisBucketRepository(BucketMapper bucketMapper) {
        this.bucketMapper = bucketMapper;
    }

    @Override
    public Bucket saveBucket(Bucket bucket) {
        bucketMapper.saveBucket(bucket);
        return bucket;
    }

    @Override
    public Thumbnail saveThumbnail(Thumbnail thumbnail, Long bucketId) {
        bucketMapper.saveThumbnail(thumbnail, bucketId);
        return thumbnail;
    }

    @Override
    public List<Tag> saveTag(List<Tag> tagList, Long bucketId) {
        bucketMapper.saveTag(tagList, bucketId);
        return tagList;
    }

    @Override
    public Optional<Bucket> findBucketById(Long bucketId) {
        return bucketMapper.findBucketById(bucketId);
    }

    @Override
    public Optional<Thumbnail> findThumbnailByBucketId(Bucket bucket) {
        return bucketMapper.findThumbnailByBucketId(bucket);
    }

    @Override
    public List<Tag> findTagByBucketId(Bucket bucket) {
        return bucketMapper.findTagByBucketId(bucket);
    }

    @Override
    public Tag findTagNameById(Tag tag) {
        return bucketMapper.findTagNameById(tag);
    }

    @Override
    public List<Tag> findAllTag() {
        return bucketMapper.findAllTag();
    }

    @Override
    public int getTotalBucketCount() {
        return bucketMapper.getTotalBucketCount();
    }

    @Override
    public List<BucketSearchResult> findAllBucket(BucketSearch bucketSearch, PageMakeVO pageMakeVO) {
        return bucketMapper.findAllBucket(bucketSearch, pageMakeVO);
    }
}
