package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketAdd {
    @NotBlank
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    @NotBlank
    private String openYn;  // 공개 여부
    @NotBlank
    private String recommendYn; // 추천 여부
    @Size(min = 1)
    private List<String> tagList; // 태그 ID들

}
