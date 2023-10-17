package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.*;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.BucketThumbnail;
import com.mybucketpet.domain.bucket.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MybatisBucketRepository implements BucketRepository {

    private final BucketMapper bucketMapper;
    @Autowired
    public MybatisBucketRepository(BucketMapper bucketMapper) {
        this.bucketMapper = bucketMapper;
    }

    @Override
    public Long saveBucket(BucketAdd bucketAdd) {
        Bucket bucket = Bucket.builder()
                .bucketTitle(bucketAdd.getBucketTitle())
                .bucketContents(bucketAdd.getBucketContents())
                .openYn(bucketAdd.getOpenYn())
                .recommendYn(bucketAdd.getRecommendYn())
                .build();

        bucketMapper.saveBucket(bucket);

        return bucket.getBucketId();
    }

    @Override
    public Long saveThumbnail(String fileOriginalName, String fileSaveName, Long bucketId) {
        BucketThumbnail thumbnail = BucketThumbnail.builder()
                .bucketThumbnailFilename(fileOriginalName)
                .bucketThumbnailSavename(fileSaveName)
                .build();
        bucketMapper.saveThumbnail(thumbnail, bucketId);
        return thumbnail.getBucketThumbnailId();
    }

    @Override
    public List<String> saveTag(Long bucketId, List<String> tagIdList) {
        List<Tag> tagList = tagIdList.stream().map((tagId) -> new Tag(Long.valueOf(tagId))).collect(Collectors.toList());
        bucketMapper.saveTag(tagList, bucketId);
        return tagIdList;
    }

    @Override
    public BucketInfo findBucketById(Long bucketId) {
        Bucket findBucket = bucketMapper.findBucketById(bucketId)
                .orElseThrow(() -> new RuntimeException("BucketAlreadyDeleted"));

        return BucketInfo.builder().bucket(findBucket).build();
    }

    @Override
    public ThumbnailInfo findThumbnailByBucketId(Long bucketId) {
        BucketThumbnail findThumbnail = bucketMapper.findThumbnailByBucketId(bucketId)
                .orElseThrow(() -> new RuntimeException("BucketThumbnail does not exist!"));

        return ThumbnailInfo.builder().bucketThumbnail(findThumbnail).build();
    }

    @Override
    public List<TagInfo> findTagByBucketId(Long bucketId) {
        List<Tag> findTagList = bucketMapper.findTagByBucketId(bucketId);

        return findTagList.stream()
                .map((tag) -> TagInfo.builder().tagId(tag.getTagId()).tagName(tag.getTagName()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TagInfo> findAllTag() {
        List<Tag> allTag = bucketMapper.findAllTag();

        return allTag.stream()
                .map((tag) -> TagInfo.builder().tagId(tag.getTagId()).tagName(tag.getTagName()).build())
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalBucketCount() {
        return bucketMapper.getTotalBucketCount();
    }

    @Override
    public List<BucketSearchResult> findAllBucket(BucketSearch bucketSearch, PageMakeVO pageMakeVO) {
        Bucket bucket = Bucket.builder()
                .openYn(bucketSearch.getOpenYn())
                .recommendYn(bucketSearch.getRecommendYn())
                .build();

        List<Tag> tagList = checkTagList(bucketSearch.getTagList());

        return bucketMapper.findAllBucket(bucketSearch.getKeywordType(), bucketSearch.getKeywordText(), bucket, tagList, pageMakeVO);
    }

    @Override
    public void deleteBucket(Long bucketId) {
        bucketMapper.deleteBucket(bucketId);
    }

    @Override
    public void deleteThumbnail(Long bucketId) {
        bucketMapper.deleteThumbnail(bucketId);
    }

    @Override
    public void deleteTag(Long bucketId) {
        bucketMapper.deleteTag(bucketId);
    }

    @Override
    public void updateBucketRecommend(Long bucketId, String recommendYn) {
        bucketMapper.updateBucketRecommend(bucketId, recommendYn);
    }

    @Override
    public void updateBucket(Long bucketId, BucketUpdate bucketUpdate) {
        Bucket bucket = Bucket.builder()
                .bucketId(bucketId)
                .bucketTitle(bucketUpdate.getBucketTitle())
                .bucketContents(bucketUpdate.getBucketContents())
                .openYn(bucketUpdate.getOpenYn())
                .recommendYn(bucketUpdate.getRecommendYn())
                .build();

        bucketMapper.updateBucket(bucketId, bucket);
    }

    @Override
    public void updateThumbnail(Long bucketId, String fileOriginalName, String fileSaveName) {
        BucketThumbnail updateThumbnail = BucketThumbnail.builder()
                .bucketThumbnailFilename(fileOriginalName)
                .bucketThumbnailSavename(fileSaveName)
                .build();

        bucketMapper.updateThumbnail(bucketId, updateThumbnail);
    }

    @Override
    public void deleteTagList(Long bucketId, List<String> deleteTagIdList) {
        List<Tag> deleteTagList = deleteTagIdList.stream().map((tagId) -> new Tag(Long.valueOf(tagId))).collect(Collectors.toList());
        bucketMapper.deleteTagList(deleteTagList, bucketId);
    }

    private List<Tag> checkTagList(List<String> tagInfoList) {
        List<Tag> tagList = null;

        if (tagInfoList != null) {
            tagList = tagInfoList.stream()
                    .map((tagId) -> new Tag(Long.valueOf(tagId))).collect(Collectors.toList());
        } else {
            tagList = new ArrayList<>();
        }

        return tagList;
    }
}
