package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.BucketSearchResult;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.BucketThumbnail;
import com.mybucketpet.domain.bucket.Tag;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {
    Bucket saveBucket(Bucket bucket);
    BucketThumbnail saveThumbnail(BucketThumbnail thumbnail, Long bucketId);
    List<Tag> saveTag(List<Tag> tagList, Long bucketId);
    Optional<Bucket> findBucketById(Long bucketId);
    Optional<BucketThumbnail> findThumbnailByBucketId(Long bucketId);
    List<Tag> findTagByBucketId(Long bucketId);
    Tag findTagNameById(Tag tag);
    List<Tag> findAllTag();
    int getTotalBucketCount();
    List<BucketSearchResult> findAllBucket(String keywordType, String keywordText,
                                           Bucket bucketSearch, List<Tag> tagList, PageMakeVO pageMakeVO);
    void deleteBucket(Long bucketId);
    void deleteThumbnail(Long bucketId);
    void deleteTag(Long bucketId);
    void updateBucketRecommend(Long bucketId, String recommendYn);
    void updateBucket(Long bucketId, Bucket bucketUpdate);
    void updateThumbnail(Long bucketId, BucketThumbnail thumbnail);
    void deleteTagList(List<Tag> deleteTagList, Long bucketId);
}
