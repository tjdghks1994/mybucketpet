package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@ToString
public class BucketInfo {
    private final Long bucketId;  // 버킷 ID
    private final String bucketTitle; // 버킷 제목
    private final String bucketContents;  // 버킷 내용
    private final String openYn;  // 공개 여부
    private final String recommendYn; // 추천 여부
    private final Long certiCnt;  // 인증 수
    private final Long scrapCnt;  // 스크랩 수
    private final Long lookupCnt; // 조회 수
    private final LocalDate modifyDate;   // 수정일자

    @Builder
    public BucketInfo(Bucket bucket) {
        this.bucketId = bucket.getBucketId();
        this.bucketTitle = bucket.getBucketTitle();
        this.bucketContents = bucket.getBucketContents();
        this.openYn = bucket.getOpenYn();
        this.recommendYn = bucket.getRecommendYn();
        this.certiCnt = bucket.getCertiCnt();
        this.scrapCnt = bucket.getScrapCnt();
        this.lookupCnt = bucket.getLookupCnt();
        this.modifyDate = bucket.getModifyDate();
    }
}
