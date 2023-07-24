package com.mybucketpet.controller.admin;

import com.mybucketpet.controller.admin.dto.BucketAdd;
import com.mybucketpet.controller.admin.dto.BucketUpdate;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.service.bucket.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/bucket")
public class AdminApiController {
    private final BucketService bucketService;
    @Value("${bucket.upload.file}")
    private String savePath;

    public AdminApiController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    /**
     * HTTP URI 설계 - API
     * 버킷 삭제                     : /admin/bucket                 DELETE
     * 버킷 등록                     : /admin/bucket/add             POST - 컨트롤 URI 형태
     * 태그 조회(목록)                : /admin/bucket/tag             GET
     * 버킷 추천 여부 변경             : /admin/bucket/recommend       PATCH
     * 버킷 수정                    : /admin/bucket/{bucketId}      PATCH
     */
    @PostMapping(value = "/add")
    public ResponseEntity<String> bucketAdd(@RequestPart("bucketAdd") BucketAdd bucketAdd,
                                            @RequestPart("thumbnailImageFile") MultipartFile thumbnailImgFile) {
        try {
            // 버킷 저장
            bucketService.save(bucketAdd, thumbnailImgFile);
        } catch (IOException e) {
            log.error("File Save Error!!", e);
            return new ResponseEntity<>("addBucketFail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("addBucketOK", HttpStatus.CREATED);
    }

    @GetMapping("/tag")
    public List<Tag> getTagList() {
        List<Tag> allTag = bucketService.findAllTag();
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
    public List<Long> updateBucketRecommend(@RequestBody List<Map<String, String>> updateBucketRecommendInfoList) {
        log.debug("updateBucketRecommendInfo = {}", updateBucketRecommendInfoList);
        List<Long> failBucketList = new ArrayList<>();
        for (Map updateBucket : updateBucketRecommendInfoList) {
            try {
                // 버킷 추천 값 변경
                bucketService.updateBucketRecommend(updateBucket);
            } catch (Exception e) {
                Long bucketId = Long.parseLong((String) updateBucket.get("bucketId"));
                failBucketList.add(bucketId);
                log.error("버킷 ID = {}", bucketId);
                log.error("버킷의 추천 값을 변경하는데 오류가 발생하였습니다.", e);
            }
        }

        return failBucketList;
    }

    @PatchMapping("/{bucketId}")
    public ResponseEntity<String> updateBucket(@PathVariable String bucketId,
                                               @RequestPart("bucketUpdate") BucketUpdate bucketUpdate,
                                               @RequestPart(value = "thumbnailImageFile", required = false) MultipartFile multipartFile) {
        log.debug("update bucketId = {}", bucketId);
        log.debug("bucketUpdate = {}", bucketUpdate);
        log.debug("thumbnailImageFile = {}", multipartFile);
        try {
            // 버킷 수정
            bucketService.updateBucket(Long.parseLong(bucketId), bucketUpdate, multipartFile);
        } catch (IOException e) {
            log.error("File Save Error!!", e);
            return new ResponseEntity<>("updateBucketFail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("updateBucketSuccess", HttpStatus.OK);
    }
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + savePath + filename);
    }
}
