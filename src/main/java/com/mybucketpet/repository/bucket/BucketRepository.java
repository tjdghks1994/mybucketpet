package com.mybucketpet.repository.bucket;

import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;

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
}
