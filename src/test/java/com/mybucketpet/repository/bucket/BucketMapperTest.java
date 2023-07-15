package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.BucketSearch;
import com.mybucketpet.controller.admin.BucketSearchResult;
import com.mybucketpet.controller.paging.PageCriteria;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Tag;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class BucketMapperTest {

    private final BucketMapper bucketMapper;
    @Autowired
    public BucketMapperTest(BucketMapper bucketMapper) {
        this.bucketMapper = bucketMapper;
    }

    @Test
    void findAllBucket() {
        // given
        List<Tag> tagList = Arrays.asList(new Tag(1L), new Tag(4L), new Tag(5L));
        BucketSearch bucketSearch = new BucketSearch("T", "test", "y", "n", tagList);
        PageCriteria pageCriteria = new PageCriteria(1);
        int total = bucketMapper.getTotalBucketCount();
        PageMakeVO pageMakeVO = new PageMakeVO(pageCriteria, total);
        // when
        List<BucketSearchResult> allBucket = bucketMapper.findAllBucket(bucketSearch, pageMakeVO);
        // then
        Assertions.assertThat(allBucket.size()).isEqualTo(1);
    }
}