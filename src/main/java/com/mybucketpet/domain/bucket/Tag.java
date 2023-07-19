package com.mybucketpet.domain.bucket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }

    @Override
    public boolean equals(Object obj) { // 태그 ID가 같으면 동일한 태그 객체
        if (!(obj instanceof Tag)) {
            throw new IllegalArgumentException("Object is not a Tag type");
        }
        Tag tag = (Tag) obj;

        return tag.getTagId() == this.tagId;
    }
}
