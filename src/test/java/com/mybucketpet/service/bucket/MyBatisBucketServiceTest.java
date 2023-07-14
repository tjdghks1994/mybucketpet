package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.BucketAdd;
import com.mybucketpet.controller.admin.BucketInfo;
import com.mybucketpet.domain.bucket.Tag;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MyBatisBucketServiceTest {

    private final BucketService service;
    @Autowired
    public MyBatisBucketServiceTest(BucketService service) {
        this.service = service;
    }
    @Test
    @DisplayName("버킷 등록 및 조회 테스트")
    void saveAndFindTest() {
        // given
        List<Tag> tagList = Arrays.asList(new Tag(1L), new Tag(2L), new Tag(3L));
        BucketAdd bucketAdd = new BucketAdd("bucketTest", "bucketContents~~~", "y",
                "y", tagList);
        // when
//        Long saveBucketId = service.save(bucketAdd);
//        BucketInfo bucketInfo = service.findById(saveBucketId);
//        // then
//        Assertions.assertThat(saveBucketId).isEqualTo(bucketInfo.getBucketId());
//        Assertions.assertThat(bucketInfo.getTagList().get(0).getTagName()).isEqualTo("강아지");
    }
}