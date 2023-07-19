package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.BucketSearch;
import com.mybucketpet.controller.admin.BucketSearchResult;
import com.mybucketpet.controller.admin.BucketUpdate;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {
    Bucket saveBucket(Bucket bucket);
    Thumbnail saveThumbnail(Thumbnail thumbnail, Long bucketId);
    List<Tag> saveTag(List<Tag> tagList, Long bucketId);
    Optional<Bucket> findBucketById(Long bucketId);
    Optional<Thumbnail> findThumbnailByBucketId(Bucket bucket);
    List<Tag> findTagByBucketId(Bucket bucket);
    Tag findTagNameById(Tag tag);
    List<Tag> findAllTag();
    int getTotalBucketCount();
    List<BucketSearchResult> findAllBucket(BucketSearch bucketSearch, PageMakeVO pageMakeVO);
    void deleteBucket(Long bucketId);
    void deleteThumbnail(Long bucketId);
    void deleteTag(Long bucketId);
    void updateBucketRecommend(Long bucketId, String recommendYn);
    void updateBucket(@Param("bucketId") Long bucketId, @Param("bu") BucketUpdate bucketUpdate);
    void updateThumbnail(@Param("bucketId") Long bucketId, @Param("thumbnail") Thumbnail thumbnail);
    void deleteTagList(@Param("deleteTag") List<Tag> deleteTagList, @Param("bucketId") Long bucketId);
}
