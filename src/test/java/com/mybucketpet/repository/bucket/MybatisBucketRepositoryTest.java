package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.*;
import com.mybucketpet.controller.paging.PageCriteria;
import com.mybucketpet.controller.paging.PageMakeVO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    @DisplayName("버킷 저장 및 조회 테스트")
    void saveBucketFindBucket() {
        // given
        BucketAdd bucketAdd = BucketAdd.builder()
                .bucketTitle("저장 테스트")
                .bucketContents("저장할 내용")
                .openYn("y")
                .recommendYn("y")
                .tagList(Arrays.asList("1", "2"))
                .build();
        String fileOriginName = "test.jpg";
        String fileSaveName = "asdkfjweilfjzxocivjzxlcv.jpg";
        // when
        Long saveBucketId = repository.saveBucket(bucketAdd);
        BucketInfo findBucket = repository.findBucketById(saveBucketId);
        repository.saveThumbnail(fileOriginName, fileSaveName, saveBucketId);
        ThumbnailInfo findThumbInfo = repository.findThumbnailByBucketId(saveBucketId);
        List<String> saveTagList = repository.saveTag(saveBucketId, bucketAdd.getTagList());
        // then
        Assertions.assertThat(saveBucketId).isEqualTo(findBucket.getBucketId());
        Assertions.assertThat(bucketAdd.getBucketTitle()).isEqualTo(findBucket.getBucketTitle());
        Assertions.assertThat(bucketAdd.getBucketContents()).isEqualTo(findBucket.getBucketContents());
        Assertions.assertThat(bucketAdd.getOpenYn()).isEqualTo(findBucket.getOpenYn());
        Assertions.assertThat(bucketAdd.getRecommendYn()).isEqualTo(findBucket.getRecommendYn());
        Assertions.assertThat(fileOriginName).isEqualTo(findThumbInfo.getThumbnailFilename());
        Assertions.assertThat(fileSaveName).isEqualTo(findThumbInfo.getThumbnailSavename());
        Assertions.assertThat(bucketAdd.getTagList().size()).isEqualTo(saveTagList.size());
    }

    @Test
    @DisplayName("조회 조건이 없는 버킷 검색 테스트")
    void noneConditionSearchBucketTest() {
        // given
        BucketSearch bucketSearch = BucketSearch.builder()
                .keywordText("")
                .keywordType("")
                .openYn("")
                .recommendYn("")
                .tagList(new ArrayList<>())
                .build();
        int totalBucketCount = repository.getTotalBucketCount();
        PageCriteria pageCriteria = new PageCriteria();
        PageMakeVO pageMakeVO = new PageMakeVO(pageCriteria, totalBucketCount);
        // when
        List<BucketSearchResult> result = repository.findAllBucket(bucketSearch, pageMakeVO);
        // then
        Assertions.assertThat(totalBucketCount).isEqualTo(result.size());
    }

    @Test
    @DisplayName("조회 조건이 있는 버킷 검색 테스트")
    void conditionSearchBucketTest() {
        // given
        BucketSearch bucketSearch = BucketSearch.builder()
                .keywordText("test")
                .keywordType("T")
                .openYn("y")
                .recommendYn("n")
                .tagList(new ArrayList<>())
                .build();
        int totalBucketCount = repository.getTotalBucketCount();
        PageCriteria pageCriteria = new PageCriteria();
        PageMakeVO pageMakeVO = new PageMakeVO(pageCriteria, totalBucketCount);
        // when
        List<BucketSearchResult> result = repository.findAllBucket(bucketSearch, pageMakeVO);
        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("버킷 삭제 테스트")
    void deleteBucket() {
        // given
        Long bucketId = 22L;
        // when
        repository.deleteThumbnail(bucketId);
        repository.deleteTag(bucketId);
        repository.deleteBucket(bucketId);
        // then
        Assertions.assertThatThrownBy(() -> repository.findBucketById(bucketId)).isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> repository.findThumbnailByBucketId(bucketId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("버킷 추천 변경 테스트")
    void changeRecommendBucket() {
        // given
        Long bucketId = 22L;
        BucketInfo findBucket = repository.findBucketById(bucketId);
        // when
        repository.updateBucketRecommend(bucketId, "y");
        BucketInfo changeBucket = repository.findBucketById(bucketId);
        // then
        Assertions.assertThat(findBucket.getRecommendYn()).isNotEqualTo(changeBucket.getRecommendYn());
    }

    @Test
    @DisplayName("버킷 수정 테스트")
    void updateBucketInfo() {
        // given
        Long bucketId = 22L;
        BucketInfo originalBucketResult = repository.findBucketById(bucketId);
        List<TagInfo> originalBucketTag = repository.findTagByBucketId(bucketId);
        BucketUpdate bucketUpdate = BucketUpdate.builder()
                .bucketTitle("update")
                .bucketContents("1111")
                .openYn("n")
                .recommendYn("y")
                .insertTagList(new ArrayList<>(Arrays.asList("1")))
                .deleteTagList(new ArrayList<>(Arrays.asList("4", "5")))
                .build();
        // when
        repository.updateBucket(bucketId, bucketUpdate);
        repository.saveTag(bucketId, bucketUpdate.getInsertTagList());
        repository.deleteTagList(bucketId, bucketUpdate.getDeleteTagList());
        BucketInfo updateBucketResult = repository.findBucketById(bucketId);
        List<TagInfo> updateBucketTagResult = repository.findTagByBucketId(bucketId);
        // then
        Assertions.assertThat(originalBucketResult.getBucketId()).isEqualTo(updateBucketResult.getBucketId());
        Assertions.assertThat(originalBucketResult.getBucketTitle()).isNotEqualTo(updateBucketResult.getBucketTitle());
        Assertions.assertThat(originalBucketResult.getBucketContents()).isNotEqualTo(updateBucketResult.getBucketContents());
        Assertions.assertThat(originalBucketResult.getOpenYn()).isNotEqualTo(updateBucketResult.getOpenYn());
        Assertions.assertThat(originalBucketResult.getRecommendYn()).isNotEqualTo(updateBucketResult.getRecommendYn());
        Assertions.assertThat(originalBucketTag.size()).isNotEqualTo(updateBucketTagResult.size());
    }

    @Test
    @DisplayName("썸네일 이미지 수정 테스트")
    void updateThumbnail() {
        // given
        Long bucketId = 22L;
        ThumbnailInfo originalThumbnail = repository.findThumbnailByBucketId(bucketId);
        String changeFileName = "zzzz.jpg";
        String changeSaveName = "/Users/3832jrdmclasdfjasdkf39zzxcvi.jpg";
        // when
        repository.updateThumbnail(bucketId, changeFileName, changeSaveName);
        ThumbnailInfo updateThumbnail = repository.findThumbnailByBucketId(bucketId);
        // then
        Assertions.assertThat(originalThumbnail.getThumbnailFilename()).isNotEqualTo(updateThumbnail.getThumbnailFilename());
        Assertions.assertThat(originalThumbnail.getThumbnailSavename()).isNotEqualTo(updateThumbnail.getThumbnailSavename());
    }
}