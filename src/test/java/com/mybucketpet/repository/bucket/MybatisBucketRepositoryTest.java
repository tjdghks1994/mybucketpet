package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.BucketUpdate;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.domain.bucket.Thumbnail;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    @DisplayName("버킷 수정 테스트")
    void bucketUpdateTest() {
        // given
        Long bucketId = 22L;
        BucketUpdate update = new BucketUpdate("updateTest", "hello~~~", "y", "y",
                Arrays.asList(new Tag(1L)), Arrays.asList(new Tag(5L)));
        Bucket bucket = new Bucket("updateTest", "hello~~~", "y", "y");
        Thumbnail updateThumb = new Thumbnail("스크린샷 2023-03-18 오전 4.58.26.png",
                "2b62de4c-6e43-4a0c-846a-33da3816c649306306.png");
        // when
        repository.updateBucket(bucketId, bucket);
        repository.updateThumbnail(bucketId, updateThumb);
        repository.saveTag(update.getInsertTagList(), bucketId);
        repository.deleteTagList(update.getDeleteTagList(), bucketId);

        Bucket findBucket = repository.findBucketById(bucketId).get();
        Thumbnail findThumbnail = repository.findThumbnailByBucketId(findBucket).get();
        List<Tag> findBucketList = repository.findTagByBucketId(findBucket);
        // then
        Assertions.assertThat(findBucket.getBucketTitle()).isEqualTo(update.getBucketTitle());
        Assertions.assertThat(findBucket.getModifyDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(findThumbnail.getThumbnailSavename()).isEqualTo(updateThumb.getThumbnailSavename());
        Assertions.assertThat(findThumbnail.getThumbnailFilename()).isEqualTo(updateThumb.getThumbnailFilename());
        Assertions.assertThat(findBucketList).contains(update.getInsertTagList().get(0));
        Assertions.assertThat(findBucketList).doesNotContain(update.getDeleteTagList().get(0));
    }
}