package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BucketAdd {
    @NotBlank
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    @NotBlank
    private String openYn;  // 공개 여부
    @NotBlank
    private String recommendYn; // 추천 여부
    @Size(min = 1)
    private List<Tag> tagList; // 태그 ID들

    public BucketAdd() { }
    public BucketAdd(String bucketTitle, String bucketContents, String openYn, String recommendYn, List<Tag> tagList) {
        this.bucketTitle = bucketTitle;
        this.bucketContents = bucketContents;
        this.openYn = openYn;
        this.recommendYn = recommendYn;
        this.tagList = tagList;
    }
}
