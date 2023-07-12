package com.mybucketpet.repository.bucket;

import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BucketMapper {
    // 버킷 등록
    void saveBucket(Bucket bucket);
    // 썸네일 등록
    void saveThumbnail(@Param("thumbnail") Thumbnail thumbnail, @Param("bucketId") Long bucketId);
    // 태그 등록
    void saveTag(@Param("tag") List<Tag> tagList, @Param("bucketId") Long bucketId);
    // 버킷 조회
    Optional<Bucket> findBucketById(Long bucketId);
    // 썸네일 조회
    Optional<Thumbnail> findThumbnailByBucketId(Bucket bucket);
    // 태그 조회
    List<Tag> findTagByBucketId(Bucket bucket);
    // 태그명 조회
    Tag findTagNameById(Tag tag);
}
