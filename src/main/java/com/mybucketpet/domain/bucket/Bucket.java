package com.mybucketpet.domain.bucket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Bucket {
    private Long bucketId;  // 버킷 ID
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    private String openYN;  // 공개 여부
    private String recommendYN; // 추천 여부
    private Long certiCnt;  // 인증 수
    private Long scrapCnt;  // 스크랩 수
    private Long lookupCnt; // 조회 수
    private LocalDate modifyDate;   // 수정일자

    public Bucket() { }

    public Bucket(String bucketTitle, String bucketContents, String openYN, String recommendYN) {
        this.bucketTitle = bucketTitle;
        this.bucketContents = bucketContents;
        this.openYN = openYN;
        this.recommendYN = recommendYN;
    }
}
