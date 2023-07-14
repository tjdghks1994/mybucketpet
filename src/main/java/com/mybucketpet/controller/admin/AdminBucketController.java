package com.mybucketpet.controller.admin;

import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.service.bucket.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/bucket")
public class AdminBucketController {
    private final BucketService bucketService;
    @Autowired
    public AdminBucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }
    /**
     * HTTP URI 설계
     * 버킷 관리 페이지 : /admin/bucket    GET
     * 버킷 등록 페이지 : /admin/bucket/add    GET
     * === HTTP API 사용 ===
     * 버킷 등록 : /admin/bucket/add    POST
     * 버킷 단일 조회 : /admin/bucket/{bucketId}  GET
     * 버킷 다중 조회(목록) : /admin/bucket/list    GET
     * 태그 조회(목록) : /admin/bucket/tag    GET
     */
    @GetMapping
    public String bucketManageForm() {
        return "admin/bucket/bucket_manage";
    }

    @GetMapping("/add")
    public String bucketManageAddForm() {
        return "admin/bucket/bucket_manage_addForm";
    }

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
    @ResponseBody
    public List<Tag> getTagList() {
        List<Tag> allTag = bucketService.findAllTag();
        log.debug("allTag = {}", allTag);

        return allTag;
    }
}
