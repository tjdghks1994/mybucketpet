package com.mybucketpet.domain.bucket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tag {
    private Long tagId; // 태그 ID
    private Long bucketId;  // 버킷 ID

    public Tag() { }

}
