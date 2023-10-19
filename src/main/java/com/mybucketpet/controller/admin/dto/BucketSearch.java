package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.*;

import java.util.List;


@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketSearch { // 버킷 목록 조회 클래스
    private String keywordType;
    private String keywordText;
    private String openYn;
    private String recommendYn;
    private List<String> tagList;

}
