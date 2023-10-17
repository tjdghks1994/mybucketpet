package com.mybucketpet.controller.admin;

import com.mybucketpet.controller.admin.dto.*;
import com.mybucketpet.controller.paging.PageCriteria;
import com.mybucketpet.controller.paging.PageMakeVO;
import com.mybucketpet.service.bucket.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/bucket")
public class AdminBucketController {
    private final BucketService bucketService;
    @Value("${bucket.upload.file}")
    private String savePath;

    @Autowired
    public AdminBucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    /**
     * HTTP URI 설계 - Form
     * 버킷 관리 페이지 (페이지 목록 조회) : /admin/bucket                     GET, POST
     * 버킷 등록 페이지                : /admin/bucket/add                 GET - 컨트롤 URI 형태
     * 버킷 수정 페이지                : /admin/bucket/{bucketId}          GET
     * 썸네일 이미지 경로               : /admin/bucket/images/{filename}   GET
     */
    @RequestMapping
    public String bucketManageList(@ModelAttribute BucketSearch bucketSearch,
                                   @ModelAttribute PageCriteria pageCriteria,
                                   Model model) {
        log.debug("bucketSearch = {}, pageCriteria = {}", bucketSearch, pageCriteria);
        if (bucketSearch == null) {
            bucketSearch = BucketSearch.builder().build();
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

    @GetMapping("/{bucketId}")
    public String updateBucketForm(@PathVariable String bucketId, Model model) {
        log.debug("bucketId = {}", bucketId);
        BucketResponse bucketResponse = bucketService.findById(Long.parseLong(bucketId));
        log.debug("bucketResponse = {}", bucketResponse);

        model.addAttribute("bucketInfo", bucketResponse);

        return "admin/bucket/bucket_manage_updateForm";
    }
}
