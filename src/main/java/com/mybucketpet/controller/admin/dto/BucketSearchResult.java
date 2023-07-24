package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ToString
public class BucketSearchResult {   // 조회한 버킷 정보를 담고 있는 클래스
    private Long rnum;
    private String bucketId;
    private String bucketTitle;
    private Long certiCnt;
    private Long scrapCnt;
    private Long lookupCnt;
    private LocalDate modifyDate;
    private String openYn;
    private String recommendYn;

    public BucketSearchResult() { }
}
