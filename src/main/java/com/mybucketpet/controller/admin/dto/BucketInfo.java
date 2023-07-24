package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * 버킷 게시글과 연관되어있는 모든 정보를 담고 있는 클래스
 * (버킷, 썸네일, 태그목록)
 */
@Getter
@Setter
@ToString
public class BucketInfo {
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
    private List<Tag> tagList;  // 태그 목록

    public BucketInfo() { }

    public BucketInfo(Long bucketId, String bucketTitle, String bucketContents, String openYn, String recommendYn,
                      Long certiCnt, Long scrapCnt, Long lookupCnt, LocalDate modifyDate, String thumbnailFilename,
                      String thumbnailSavename, List<Tag> tagList) {
        this.bucketId = bucketId;
        this.bucketTitle = bucketTitle;
        this.bucketContents = bucketContents;
        this.openYn = openYn;
        this.recommendYn = recommendYn;
        this.certiCnt = certiCnt;
        this.scrapCnt = scrapCnt;
        this.lookupCnt = lookupCnt;
        this.modifyDate = modifyDate;
        this.thumbnailFilename = thumbnailFilename;
        this.thumbnailSavename = thumbnailSavename;
        this.tagList = tagList;
    }
}
