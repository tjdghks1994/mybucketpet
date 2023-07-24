package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class BucketSearch { // 버킷 목록 조회 클래스
    private String keywordType;
    private String keywordText;
    private String openYn;
    private String recommendYn;
    private List<Tag> tagList;

    public BucketSearch() { }

    public BucketSearch(String keywordType, String keywordText, String openYn, String recommendYn, List<Tag> tagList) {
        this.keywordType = keywordType;
        this.keywordText = keywordText;
        this.openYn = openYn;
        this.recommendYn = recommendYn;
        this.tagList = tagList;
    }
}
