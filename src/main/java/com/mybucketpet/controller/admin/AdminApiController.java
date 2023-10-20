package com.mybucketpet.controller.admin;

import com.mybucketpet.controller.admin.dto.*;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.exception.ErrorResult;
import com.mybucketpet.exception.bucket.BucketException;
import com.mybucketpet.exception.bucket.SaveFailThumbnailException;
import com.mybucketpet.service.bucket.BucketService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/admin/buckets")
public class AdminApiController {
    private final BucketService bucketService;
    private final MessageSource messageSource;
    @Value("${bucket.upload.file}")
    private String savePath;

    public AdminApiController(BucketService bucketService, MessageSource messageSource) {
        this.bucketService = bucketService;
        this.messageSource = messageSource;
    }

    @ExceptionHandler(SaveFailThumbnailException.class)
    public ResponseEntity<ErrorResult> saveThumbnailExceptionHandler(
            final SaveFailThumbnailException saveFailThumbnailException) {
        log.error(saveFailThumbnailException.getMessage());
        return ResponseEntity.internalServerError()
                .body(ErrorResult.from(saveFailThumbnailException.getMessage()));
    }

    @ExceptionHandler(BucketException.class)
    public ResponseEntity<ErrorResult> bucketExceptionHandler(BucketException bucketException) {
        log.error(bucketException.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResult.from(bucketException.getMessage()));
    }

    /**
     * HTTP URI 설계 - API
     * 버킷 목록 조회            /admin/buckets                          GET
     * 버킷 등록                /admin/buckets                          POST
     * 버킷 단건 조회            /admin/buckets/{bucketId}               GET
     * 버킷 수정               /admin/buckets/{bucketId}                PATCH
     * 버킷 삭제               /admin/buckets/{bucketId}                DELETE
     * 버킷 추천 여부 변경       /admin/buckets/{bucketId}/recommends     PATCH
     * 버킷 썸네일 이미지 조회     /admin/buckets/thumbnails/{filename}     GET
     * 버킷 태그 조회           /admin/buckets/tags                      GET
     */

    // 버킷 목록 조회
    @GetMapping
    public ResponseEntity<List<BucketSearchResult>> bucketManageList(@RequestBody BucketSearch bucketSearch) {
        log.debug("bucketSearch = {}", bucketSearch);
        if (bucketSearch == null) {
            bucketSearch = BucketSearch.builder().build();
        }

        int totalCnt = bucketService.getTotalBucketCount();
        PageMakeVO pageMakeVO = new PageMakeVO(bucketSearch, totalCnt);
        List<BucketSearchResult> bucketList = bucketService.findAllBucket(bucketSearch, pageMakeVO);

        return ResponseEntity.ok(bucketList);
    }

    // 버킷 등록
    @PostMapping
    public ResponseEntity<Void> bucketAdd(
            @Valid @RequestPart("bucketAdd") BucketAdd bucketAdd,
            @RequestPart(value = "thumbnailImageFile", required = false) MultipartFile thumbnailImgFile) throws IOException {

        // 버킷 저장
        Long saveBucketId = bucketService.save(bucketAdd, thumbnailImgFile);
        URI location = URI.create("/admin/buckets/" + saveBucketId);

        return ResponseEntity.created(location).build();
    }

    // 버킷 단건 조회
    @GetMapping("/{bucketId}")
    public ResponseEntity<BucketResponse> bucketDetailInfo(@PathVariable Long bucketId) {
        BucketResponse findBucket = bucketService.findById(bucketId);

        return ResponseEntity.ok(findBucket);
    }

    // 버킷 수정
    @PatchMapping("/{bucketId}")
    public ResponseEntity<BucketResponse> updateBucket(@PathVariable Long bucketId,
                           @Validated @RequestPart("bucketUpdate") BucketUpdate bucketUpdate,
                           @RequestPart(value = "thumbnailImageFile", required = false) MultipartFile multipartFile)
            throws IOException {
        log.debug("update bucketId = {}", bucketId);
        log.debug("bucketUpdate = {}", bucketUpdate);
        log.debug("thumbnailImageFile = {}", multipartFile);

        // 버킷 수정
        bucketService.updateBucket(bucketId, bucketUpdate, multipartFile);
        BucketResponse updateBucket = bucketService.findById(bucketId);

        return ResponseEntity.ok(updateBucket);
    }

    // 버킷 삭제
    @DeleteMapping("/{bucketId}")
    public ResponseEntity<Void> deleteBucketList(@PathVariable Long bucketId) {
        log.debug("bucketId = {}", bucketId);
        // 버킷 삭제
        bucketService.deleteBucket(bucketId);

        return ResponseEntity.noContent().build();
    }

    // 버킷 추천 여부 변경
    @PatchMapping("/{bucketId}/recommends")
    @ResponseBody
    public ResponseEntity<Void> updateBucketRecommend(@PathVariable Long bucketId,
                                            @RequestBody String changeRecommendValue) {
        log.debug("bucketId = {}, changeRecommendValue={}", bucketId, changeRecommendValue);
        bucketService.updateBucketRecommend(bucketId, changeRecommendValue);

        return ResponseEntity.ok().build();
    }

    // 버킷 썸네일 이미지 조회
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + savePath + filename);
    }
    // 버킷 태그 조회
    @GetMapping("/tags")
    public List<TagInfo> getTagList() {
        List<TagInfo> allTag = bucketService.findAllTag();
        log.debug("allTag = {}", allTag);

        return allTag;
    }

}
