package com.mybucketpet.service.bucket;

import com.mybucketpet.controller.admin.dto.BucketAdd;
import com.mybucketpet.controller.admin.dto.BucketResponse;
import com.mybucketpet.controller.admin.dto.BucketUpdate;
import com.mybucketpet.domain.bucket.Tag;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
class BucketServiceImplTest {

    private final BucketService service;
    @Autowired
    public BucketServiceImplTest(BucketService service) {
        this.service = service;
    }
    @Test
    @DisplayName("버킷 등록 및 조회 테스트")
    void saveAndFindTest() throws IOException {
        // given
        BucketAdd bucketAdd = BucketAdd.builder()
                .bucketTitle("등록 테스트")
                .bucketContents("버킷 등록 테스트 내용 입니다")
                .openYn("y")
                .recommendYn("n")
                .tagList(Arrays.asList("1", "2", "3"))
                .build();
        MockMultipartFile thumbnailImageFile = getMockMultipartFile();
        // when
        Long saveBucketId = service.save(bucketAdd, thumbnailImageFile);
        BucketResponse findBucket = service.findById(saveBucketId);
        // then
        Assertions.assertThat(bucketAdd.getBucketTitle()).isEqualTo(findBucket.getBucketTitle());
        Assertions.assertThat(bucketAdd.getBucketContents()).isEqualTo(findBucket.getBucketContents());
        Assertions.assertThat(bucketAdd.getOpenYn()).isEqualTo(findBucket.getOpenYn());
        Assertions.assertThat(bucketAdd.getRecommendYn()).isEqualTo(findBucket.getRecommendYn());
        Assertions.assertThat(bucketAdd.getTagList().size()).isEqualTo(findBucket.getTagList().size());
        Assertions.assertThat(thumbnailImageFile.getOriginalFilename()).isEqualTo(findBucket.getThumbnailFilename());
    }

    @Test
    @DisplayName("버킷 수정 테스트")
    void updateBucket() throws IOException {
        // given
        Long bucketId = 22L;
        BucketUpdate bucketUpdate = BucketUpdate
                .builder()
                .bucketTitle("버킷 수정합니다")
                .bucketContents("버킷 내용 수정입니다.")
                .openYn("n")
                .recommendYn("y")
                .insertTagList(Arrays.asList("1", "2"))
                .deleteTagList(Arrays.asList("4"))
                .build();
        MockMultipartFile thumbnailImageFile = getMockMultipartFile();
        // when
        BucketResponse originalFindBucket = service.findById(bucketId);
        service.updateBucket(bucketId, bucketUpdate, thumbnailImageFile);
        BucketResponse updateFindBucket = service.findById(bucketId);
        // then
        Assertions.assertThat(bucketUpdate.getBucketTitle()).isEqualTo(updateFindBucket.getBucketTitle());
        Assertions.assertThat(bucketUpdate.getBucketContents()).isEqualTo(updateFindBucket.getBucketContents());
        Assertions.assertThat(bucketUpdate.getOpenYn()).isEqualTo(updateFindBucket.getOpenYn());
        Assertions.assertThat(bucketUpdate.getRecommendYn()).isEqualTo(updateFindBucket.getRecommendYn());
        Assertions.assertThat(originalFindBucket.getTagList().size()).isNotEqualTo(updateFindBucket.getTagList().size());
        Assertions.assertThat(originalFindBucket.getThumbnailFilename()).isNotEqualTo(updateFindBucket.getThumbnailFilename());
    }

    /**
     * MultiPartFile 파라미터가 필요하여 MockMultipartFile 객체 생성 메서드
     */
    private MockMultipartFile getMockMultipartFile() throws IOException {
        String fileName = "test";
        String contentType = "png";
        String filePath = "/Users/parksunghwan/desktop/projects/mbpuploadfile/bucket/test.png";
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));

        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}