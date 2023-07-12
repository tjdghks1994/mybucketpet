package com.mybucketpet.domain.bucket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Thumbnail {
    private Long thumbnailId;   // 썸네일 ID
    private String thumbnailFilename;   // 썸네일 이미지 원본파일명
    private String thumbnailSavename;   // 썸네일 저장파일명

    public Thumbnail() { }

    public Thumbnail(String thumbnailFilename, String thumbnailSavename) {
        this.thumbnailFilename = thumbnailFilename;
        this.thumbnailSavename = thumbnailSavename;
    }
}
