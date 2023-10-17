package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketUpdate {
    @NotBlank
    private final String bucketTitle; // 버킷 제목
    private final String bucketContents;  // 버킷 내용
    @NotBlank
    private final String openYn;  // 공개 여부
    @NotBlank
    private final String recommendYn; // 추천 여부
    private final List<String> insertTagList; // 새로 추가된 태그 ID 목록
    private final List<String> deleteTagList; // 삭제 할 태그 ID 목록
}
