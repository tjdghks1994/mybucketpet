package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.BucketSearchResult;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.BucketThumbnail;
import com.mybucketpet.domain.bucket.Tag;
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
    public BucketThumbnail saveThumbnail(BucketThumbnail thumbnail, Long bucketId) {
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
    public Optional<BucketThumbnail> findThumbnailByBucketId(Long bucketId) {
        return bucketMapper.findThumbnailByBucketId(bucketId);
    }

    @Override
    public List<Tag> findTagByBucketId(Long bucketId) {
        return bucketMapper.findTagByBucketId(bucketId);
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
    public List<BucketSearchResult> findAllBucket(String keywordType, String keywordText,
                                                  Bucket bucketSearch, List<Tag> tagList,  PageMakeVO pageMakeVO) {
        return bucketMapper.findAllBucket(keywordType, keywordText, bucketSearch, tagList, pageMakeVO);
    }

    @Override
    public void deleteBucket(Long bucketId) {
        bucketMapper.deleteBucket(bucketId);
    }

    @Override
    public void deleteThumbnail(Long bucketId) {
        bucketMapper.deleteThumbnail(bucketId);
    }

    @Override
    public void deleteTag(Long bucketId) {
        bucketMapper.deleteTag(bucketId);
    }

    @Override
    public void updateBucketRecommend(Long bucketId, String recommendYn) {
        bucketMapper.updateBucketRecommend(bucketId, recommendYn);
    }

    @Override
    public void updateBucket(Long bucketId, Bucket bucketUpdate) {
        bucketMapper.updateBucket(bucketId, bucketUpdate);
    }

    @Override
    public void updateThumbnail(Long bucketId, BucketThumbnail thumbnail) {
        bucketMapper.updateThumbnail(bucketId, thumbnail);
    }

    @Override
    public void deleteTagList(List<Tag> deleteTagList, Long bucketId) {
        bucketMapper.deleteTagList(deleteTagList, bucketId);
    }
}
