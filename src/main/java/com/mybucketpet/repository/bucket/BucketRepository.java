package com.mybucketpet.repository.bucket;

import com.mybucketpet.controller.admin.dto.*;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Bucket;
import com.mybucketpet.domain.bucket.BucketThumbnail;
import com.mybucketpet.domain.bucket.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {
    Long saveBucket(BucketAdd bucketAdd);
    Long saveThumbnail(String fileOriginalName, String fileSaveName, Long bucketId);
    List<String> saveTag(Long bucketId, List<String> tagIdList);
    BucketInfo findBucketById(Long bucketId);
    ThumbnailInfo findThumbnailByBucketId(Long bucketId);
    List<TagInfo> findTagByBucketId(Long bucketId);
    List<TagInfo> findAllTag();
    int getTotalBucketCount();
    List<BucketSearchResult> findAllBucket(BucketSearch bucketSearch, PageMakeVO pageMakeVO);
    void deleteBucket(Long bucketId);
    void deleteThumbnail(Long bucketId);
    void deleteTag(Long bucketId);
    void updateBucketRecommend(Long bucketId, String recommendYn);
    void updateBucket(Long bucketId, BucketUpdate bucketUpdate);
    void updateThumbnail(Long bucketId, String fileOriginalName, String fileSaveName);
    void deleteTagList(Long bucketId, List<String> deleteTagIdList);
}
