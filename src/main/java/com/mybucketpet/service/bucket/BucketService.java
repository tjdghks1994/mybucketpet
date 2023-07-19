package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.*;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BucketService {
    // 버킷 등록 - 등록 완료시 완료된 버킷의 Id 값 반환
    Long save(BucketAdd bucketAdd, MultipartFile file) throws IOException;
    // 버킷 단일 조회
    BucketInfo findById(Long bucketId);
    // 모든 태크 목록 조회
    List<Tag> findAllTag();
    // 모든 버킷 수
    int getTotalBucketCount();
    // 조회 조건에 맞는 모든 버킷 목록 조회
    List<BucketSearchResult> findAllBucket(@Param("bs") BucketSearch bucketSearch, @Param("page") PageMakeVO pageMakeVO);
    // 버킷 삭제
    void deleteBucket(Long bucketId);
    // 버킷 추천 여부 변경
    void updateBucketRecommend(Map<String, String> updateBucketList);
    // 버킷 수정
    void updateBucket(Long bucketId, BucketUpdate bucketUpdate, MultipartFile multipartFile) throws IOException;
}
