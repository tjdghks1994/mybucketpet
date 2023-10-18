package com.mybucketpet.controller.admin.dto;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketRecommendInfo {
    private final Long bucketId;
    private final String recommendYn;
    private final String changeRecommendYn;
}
