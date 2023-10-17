package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import lombok.*;

import java.util.List;


@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketSearch { // 버킷 목록 조회 클래스
    private final String keywordType;
    private final String keywordText;
    private final String openYn;
    private final String recommendYn;
    private List<String> tagList;

}
