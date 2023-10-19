package com.mybucketpet.controller.admin.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketRecommendInfo {
    private Long bucketId;
    private String recommendYn;
    private String changeRecommendYn;
}
