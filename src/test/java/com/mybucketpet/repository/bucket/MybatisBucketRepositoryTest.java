package com.mybucketpet.repository.bucket;

import com.mybucketpet.config.AppConfig;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Slf4j
@SpringBootTest
class MybatisBucketRepositoryTest {

    private final BucketRepository repository;

    @Autowired
    MybatisBucketRepositoryTest(BucketRepository repository) {
        this.repository = repository;
    }

    @Test
    @DisplayName("버킷, 썸네일, 태그 조회 및 저장 테스트")
    void saveAndFindTest() {
        // given
        Bucket bucket = new Bucket("test", "content test", "y", "n");
        Thumbnail thumbnail = new Thumbnail("test.png", "asdfjcivjzoiawe.png");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(1L));
        tagList.add(new Tag(2L));
        tagList.add(new Tag(3L));
        tagList.add(new Tag(4L));
        // when
        // 저장
        Bucket saveBucket = repository.saveBucket(bucket);
        Thumbnail saveThumbnail = repository.saveThumbnail(thumbnail, saveBucket.getBucketId());
        List<Tag> saveTagList = repository.saveTag(tagList, saveBucket.getBucketId());
        // 조회
        Bucket findBucket = repository.findBucketById(saveBucket.getBucketId()).get();
        Thumbnail findThumbnail = repository.findThumbnailByBucketId(saveBucket).get();
        List<Tag> findTagList = repository.findTagByBucketId(saveBucket);
        Tag findTagFirst = repository.findTagNameById(findTagList.get(0));

        // then
        Assertions.assertThat(saveBucket.getBucketId()).isEqualTo(findBucket.getBucketId());
        Assertions.assertThat(saveThumbnail.getThumbnailId()).isEqualTo(findThumbnail.getThumbnailId());
        Assertions.assertThat(saveTagList.size()).isEqualTo(findTagList.size());
        Assertions.assertThat(findTagFirst.getTagName()).isEqualTo("강아지");
    }

}