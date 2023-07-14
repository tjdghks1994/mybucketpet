package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.BucketAdd;
import com.mybucketpet.controller.admin.BucketInfo;
import com.mybucketpet.domain.bucket.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BucketService {
    // 버킷 등록 - 등록 완료시 완료된 버킷의 Id 값 반환
    Long save(BucketAdd bucketAdd, MultipartFile file) throws IOException;
    // 버킷 단일 조회
    BucketInfo findById(Long bucketId);
    // 모든 태크 목록 조회
    List<Tag> findAllTag();
}
