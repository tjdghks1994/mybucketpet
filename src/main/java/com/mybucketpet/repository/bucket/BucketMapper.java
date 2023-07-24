package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.BucketSearch;
import com.mybucketpet.controller.admin.dto.BucketSearchResult;
import com.mybucketpet.controller.admin.dto.BucketUpdate;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

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
    // 모든 태그 목록 조회
    List<Tag> findAllTag();
    // 전체 버킷 수
    int getTotalBucketCount();
    // 버킷 목록 조회
    List<BucketSearchResult> findAllBucket(@Param("keywordType") String keywordType, @Param("keywordText") String keywordText,
                                           @Param("bs") Bucket bucketSearch, @Param("tl") List<Tag> tagList,
                                           @Param("page") PageMakeVO pageMakeVO);
    // 버킷 삭제
    void deleteBucket(Long bucketId);
    // 썸네일 삭제 - 삭제된 버킷 ID를 가지고 있는 썸네일
    void deleteThumbnail(Long bucketId);
    // 태그 삭제 - 삭제된 버킷 ID를 가지고 있는 태그
    void deleteTag(Long bucketId);
    // 추천 버킷으로 변경/해제
    void updateBucketRecommend(@Param("bucketId") Long bucketId, @Param("recommendYn") String recommendYn);
    // 버킷 수정
    void updateBucket(@Param("bucketId") Long bucketId, @Param("bu") Bucket bucketUpdate);
    // 썸네일 수정
    void updateThumbnail(@Param("bucketId") Long bucketId, @Param("thumbnail") Thumbnail thumbnail);
    // 태그 수정 - 수정의 경우 삭제된 태그 목록을 삭제 처리하고, 새로 추가된 태그 목록은 태그 추가 메서드를 사용!
    void deleteTagList(@Param("deleteTag") List<Tag> deleteTagList, @Param("bucketId") Long bucketId);
}
