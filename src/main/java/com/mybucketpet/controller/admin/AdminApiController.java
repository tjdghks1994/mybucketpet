package com.mybucketpet.controller.admin;

import com.mybucketpet.controller.admin.dto.BucketAdd;
import com.mybucketpet.controller.admin.dto.BucketRecommendInfo;
import com.mybucketpet.controller.admin.dto.BucketUpdate;
import com.mybucketpet.controller.admin.dto.TagInfo;
import com.mybucketpet.exception.ErrorResult;
import com.mybucketpet.service.bucket.BucketService;
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
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/admin/bucket")
public class AdminApiController {
    private final BucketService bucketService;
    private final MessageSource messageSource;
    @Value("${bucket.upload.file}")
    private String savePath;

    public AdminApiController(BucketService bucketService, MessageSource messageSource) {
        this.bucketService = bucketService;
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ErrorResult ioExceptionHandler(IOException ioException) {
        log.error("[ioExceptionHandler]", ioException);
        return new ErrorResult("FileSaveError", "파일을 저장하는데 오류가 발생했습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult exceptionHandler(Exception e) {
        log.error("[exceptionHandler]", e);
        return new ErrorResult("ServerError", "내부 오류가 발생했습니다!");
    }

    /**
     * HTTP URI 설계 - API
     * 버킷 단건 조회            /admin/buckets/{bucketId}
     * 버킷 목록 조회            /admin/buckets
     * 버킷 등록               /admin/buckets
     * 버킷 수정               /admin/buckets/{bucketId}
     * 버킷 삭제               /admin/buckets/{bucketId}
     * 버킷 썸네일 이미지 조회     /admin/buckets/thumbnails/{filename}
     * 버킷 태그 조회           /admin/buckets/tags
     * 버킷 추천 여부 변경       /admin/buckets/recommends
     */
    @PostMapping(value = "/add")
    public ResponseEntity<String> bucketAdd(@Validated @RequestPart("bucketAdd") BucketAdd bucketAdd, BindingResult bindingResult,
                                            @RequestPart(value = "thumbnailImageFile", required = false) MultipartFile thumbnailImgFile) throws IOException {
        // 검증 추가
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult = {}", bindingResult);
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            String errorCode = Arrays.stream(fieldErrors.get(0).getCodes()).findFirst().get();
            // bindingResult에서 반환되는 필드 에러가 발생한 코드 값을 이용해서 messageSource를 활용해 errors.properties에 등록된 값 가져오기
            String validMessage = messageSource.getMessage(errorCode, null, Locale.KOREA);
            
            return new ResponseEntity<>(validMessage, HttpStatus.BAD_REQUEST);
        }
        // 썸네일 이미지를 첨부하지 않은 경우 검증
        if (thumbnailImgFile == null) {
            return new ResponseEntity<>(
                    messageSource.getMessage("Size.multipartFile.thumbnailImage", null, Locale.KOREA),
                    HttpStatus.BAD_REQUEST
            );
        }

        // 버킷 저장
        bucketService.save(bucketAdd, thumbnailImgFile);

        return new ResponseEntity<>("addBucketOK", HttpStatus.CREATED);
    }

    @GetMapping("/tag")
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
