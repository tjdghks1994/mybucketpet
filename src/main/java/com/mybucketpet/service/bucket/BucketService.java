package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.BucketAdd;
import com.mybucketpet.controller.admin.BucketInfo;

import java.util.Optional;

public interface BucketService {
    // 버킷 등록 - 등록 완료시 완료된 버킷의 Id 값 반환
    Long save(BucketAdd bucketAdd);

    // 버킷 단일 조회
    BucketInfo findById(Long bucketId);
}
