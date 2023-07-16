package com.mybucketpet.controller.admin;

import com.mybucketpet.controller.paging.PageCriteria;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.domain.bucket.Tag;
import com.mybucketpet.service.bucket.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
     * 버킷 관리 페이지 (페이지 목록 조회) : /admin/bucket                GET, POST
     * 버킷 등록 페이지                : /admin/bucket/add            GET - 컨트롤 URI 형태
     * === HTTP API 사용 ===
     * 버킷 삭제                    : /admin/bucket                 DELETE
     * 버킷 등록                    : /admin/bucket/add             POST - 컨트롤 URI 형태
     * 버킷 단일 조회                 : /admin/bucket/{bucketId}      GET
     * 태그 조회(목록)                : /admin/bucket/tag             GET
     */
    @RequestMapping
    public String bucketManageList(@ModelAttribute BucketSearch bucketSearch,
                                   @ModelAttribute PageCriteria pageCriteria,
                                   Model model) {
        log.debug("bucketSearch = {}, pageCriteria = {}", bucketSearch, pageCriteria);
        if (bucketSearch == null) {
            bucketSearch = new BucketSearch();
        }
        if (pageCriteria == null) {
            pageCriteria = new PageCriteria();
        }

        int totalCnt = bucketService.getTotalBucketCount();
        PageMakeVO pageMakeVO = new PageMakeVO(pageCriteria, totalCnt);
        List<BucketSearchResult> bucketList = bucketService.findAllBucket(bucketSearch, pageMakeVO);
        log.debug("bucketList = {}", bucketList);
        log.debug("pageMakeVO = {}", pageMakeVO);

        model.addAttribute("bucketList", bucketList);
        model.addAttribute("pageMaker", pageMakeVO);

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

    @DeleteMapping
    @ResponseBody
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
}
