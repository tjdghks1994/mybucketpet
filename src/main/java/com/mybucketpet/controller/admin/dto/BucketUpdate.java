package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketUpdate {
    @NotBlank
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    @NotBlank
    private String openYn;  // 공개 여부
    @NotBlank
    private String recommendYn; // 추천 여부
    private List<String> insertTagList; // 새로 추가된 태그 ID 목록
    private List<String> deleteTagList; // 삭제 할 태그 ID 목록
}
