package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class BucketUpdate {
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    private String openYn;  // 공개 여부
    private String recommendYn; // 추천 여부
    private List<Tag> insertTagList; // 새로 추가된 태그 ID 목록
    private List<Tag> deleteTagList; // 삭제 할 태그 ID 목록

    public BucketUpdate() { }

    public BucketUpdate(String bucketTitle, String bucketContents, String openYn,
                        String recommendYn, List<Tag> insertTagList, List<Tag> deleteTagList) {
        this.bucketTitle = bucketTitle;
        this.bucketContents = bucketContents;
        this.openYn = openYn;
        this.recommendYn = recommendYn;
        this.insertTagList = insertTagList;
        this.deleteTagList = deleteTagList;
    }
}
