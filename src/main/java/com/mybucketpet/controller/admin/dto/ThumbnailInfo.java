package com.mybucketpet.controller.admin.dto;

import com.mybucketpet.domain.bucket.BucketThumbnail;
import lombok.*;

@Getter
@ToString
public class ThumbnailInfo {
    private final String thumbnailFilename;   // 썸네일 이미지 원본파일명
    private final String thumbnailSavename;   // 썸네일 저장파일명

    @Builder
    public ThumbnailInfo(BucketThumbnail bucketThumbnail) {
        this.thumbnailFilename = bucketThumbnail.getBucketThumbnailFilename();
        this.thumbnailSavename = bucketThumbnail.getBucketThumbnailSavename();
    }
}
