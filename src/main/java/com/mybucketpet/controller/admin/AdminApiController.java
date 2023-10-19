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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
     * 버킷 등록               /admin/buckets                           POST
     * 버킷 복수 삭제           /admin/buckets                           DELETE
     * 버킷 단건 조회            /admin/buckets/{bucketId}               GET
     * 버킷 수정               /admin/buckets/{bucketId}                PATCH
     * 버킷 단건 삭제           /admin/buckets/{bucketId}                DELETE
     * 버킷 썸네일 이미지 조회     /admin/buckets/thumbnails/{filename}     GET
     * 버킷 태그 조회           /admin/buckets/tags                      GET
     * 버킷 추천 여부 변경       /admin/buckets/recommends                PATCH
     */

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

    @PostMapping
    public ResponseEntity<Void> bucketAdd(
            @Valid @RequestPart("bucketAdd") BucketAdd bucketAdd,
            @RequestPart(value = "thumbnailImageFile", required = false) MultipartFile thumbnailImgFile) throws IOException {

        // 버킷 저장
        Long saveBucketId = bucketService.save(bucketAdd, thumbnailImgFile);
        URI location = URI.create("/admin/buckets/" + saveBucketId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/tags")
    public List<TagInfo> getTagList() {
        List<TagInfo> allTag = bucketService.findAllTag();
        log.debug("allTag = {}", allTag);

        return allTag;
    }

    @DeleteMapping
    public List<Long> deleteBucketList(@RequestBody List<Long> bucketIdList) {
        log.debug("bucketIdList = {}", bucketIdList);
        List<Long> failBucketList = new ArrayList<>();
        for (Long bucketId : bucketIdList) {
            try {
                // 버킷 삭제
                bucketService.deleteBucket(bucketId);
            } catch (Exception e) {
                failBucketList.add(bucketId);
                log.error("버킷 ID = {}", bucketId);
                log.error("버킷을 삭제하는데 오류가 발생하였습니다.", e);
            }
        }

        return failBucketList;
    }

    @PatchMapping("/recommend")
    @ResponseBody
    public List<Long> updateBucketRecommend(@RequestBody List<BucketRecommendInfo> updateBucketRecommendInfoList) {
        log.debug("updateBucketRecommendInfo = {}", updateBucketRecommendInfoList);
        List<Long> failBucketList = new ArrayList<>();
        for (BucketRecommendInfo updateBucket : updateBucketRecommendInfoList) {
            try {
                // 버킷 추천 값 변경
                bucketService.updateBucketRecommend(updateBucket);
            } catch (Exception e) {
                Long bucketId = updateBucket.getBucketId();
                failBucketList.add(bucketId);
                log.error("버킷 ID = {}", bucketId);
                log.error("버킷의 추천 값을 변경하는데 오류가 발생하였습니다.", e);
            }
        }

        return failBucketList;
    }

    @PatchMapping("/{bucketId}")
    public ResponseEntity<String> updateBucket(@PathVariable String bucketId,
                                               @Validated @RequestPart("bucketUpdate") BucketUpdate bucketUpdate,
                                               BindingResult bindingResult,
                                               @RequestPart(value = "thumbnailImageFile", required = false) MultipartFile multipartFile)
                                                throws IOException {
        log.debug("update bucketId = {}", bucketId);
        log.debug("bucketUpdate = {}", bucketUpdate);
        log.debug("thumbnailImageFile = {}", multipartFile);

        if (bindingResult.hasErrors()) {
            log.debug("bindingResult = {}", bindingResult);
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            String errorCode = Arrays.stream(fieldErrors.get(0).getCodes()).findFirst().get();
            // bindingResult에서 반환되는 필드 에러가 발생한 코드 값을 이용해서 messageSource를 활용해 errors.properties에 등록된 값 가져오기
            String validMessage = messageSource.getMessage(errorCode, null, Locale.KOREA);

            return new ResponseEntity<>(validMessage, HttpStatus.BAD_REQUEST);
        }

        // 버킷 수정
        bucketService.updateBucket(Long.parseLong(bucketId), bucketUpdate, multipartFile);

        return new ResponseEntity<>("updateBucketSuccess", HttpStatus.OK);
    }
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + savePath + filename);
    }
}
