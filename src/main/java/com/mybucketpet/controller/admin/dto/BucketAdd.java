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
    @NotBlank(message = "버킷 제목을 입력해주세요.")
    private String bucketTitle; // 버킷 제목
    private String bucketContents;  // 버킷 내용
    @NotBlank(message = "버킷의 공개여부를 선택해주세요.")
    private String openYn;  // 공개 여부
    @NotBlank(message = "버킷의 추천여부를 선택해주세요.")
    private String recommendYn; // 추천 여부
    @Size(min = 1, message = "버킷의 태그는 최소 1개이상 선택해주세요.")
    private List<String> tagList; // 태그 ID들

}
