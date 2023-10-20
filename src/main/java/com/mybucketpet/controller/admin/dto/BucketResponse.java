package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


/**
 * 버킷 게시글과 연관되어있는 모든 정보를 담고 있는 클래스
 * (버킷, 썸네일, 태그목록)
 */
@Getter
@ToString
public class BucketResponse {
    private Long bucketId;  // 버킷 ID
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    private String openYn;  // 공개 여부
    private String recommendYn; // 추천 여부
    private Long certiCnt;  // 인증 수
    private Long scrapCnt;  // 스크랩 수
    private Long lookupCnt; // 조회 수
    private LocalDate modifyDate;   // 수정일자
    private String thumbnailFilename;   // 썸네일 이미지 원본파일명
    private String thumbnailSavename;   // 썸네일 저장파일명
    private List<TagInfo> tagList;  // 태그 목록

    @Builder
    public BucketResponse(BucketInfo bucketInfo, ThumbnailInfo thumbnailInfo, List<TagInfo> tagList) {
        this.bucketId = bucketInfo.getBucketId();
        this.bucketTitle = bucketInfo.getBucketTitle();
        this.bucketContents = bucketInfo.getBucketContents();
        this.openYn = bucketInfo.getOpenYn();
        this.recommendYn = bucketInfo.getRecommendYn();
        this.certiCnt = bucketInfo.getCertiCnt();
        this.scrapCnt = bucketInfo.getScrapCnt();
        this.lookupCnt = bucketInfo.getLookupCnt();
        this.modifyDate = bucketInfo.getModifyDate();
        this.thumbnailFilename = thumbnailInfo.getThumbnailFilename();
        this.thumbnailSavename = thumbnailInfo.getThumbnailSavename();
        this.tagList = tagList;
    }
}
