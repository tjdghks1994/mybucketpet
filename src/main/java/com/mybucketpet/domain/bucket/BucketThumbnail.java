package com.mybucketpet.domain.bucket;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketThumbnail {
    private Long bucketThumbnailId;   // 썸네일 ID
    private String bucketThumbnailFilename;   // 썸네일 이미지 원본파일명
    private String bucketThumbnailSavename;   // 썸네일 저장파일명

}
