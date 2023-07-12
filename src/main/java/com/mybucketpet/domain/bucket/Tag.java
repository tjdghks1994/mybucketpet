package com.mybucketpet.domain.bucket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tag {
    private Long tagId; // 태그 ID
    private String tagName; // 태그 명

    public Tag() { }

    public Tag(Long tagId) {
        this.tagId = tagId;
    }
}
