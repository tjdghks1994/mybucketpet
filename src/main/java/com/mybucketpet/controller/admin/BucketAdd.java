package com.mybucketpet.controller.admin;

import com.mybucketpet.domain.bucket.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BucketAdd {
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    private String openYn;  // 공개 여부
    private String recommendYn; // 추천 여부
    private String thumbnailFilename;   // 썸네일 이미지 원본파일명
    private String thumbnailSavename;   // 썸네일 저장파일명
    private List<Tag> tagList; // 태그 ID들

    public BucketAdd() { }
    public BucketAdd(String bucketTitle, String bucketContents, String openYn, String recommendYn,
                     String thumbnailFilename, String thumbnailSavename, List<Tag> tagList) {
        this.bucketTitle = bucketTitle;
        this.bucketContents = bucketContents;
        this.openYn = openYn;
        this.recommendYn = recommendYn;
        this.thumbnailFilename = thumbnailFilename;
        this.thumbnailSavename = thumbnailSavename;
        this.tagList = tagList;
    }
}
