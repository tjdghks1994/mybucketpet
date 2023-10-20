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

    @Autowired
    public AdminBucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    /**
     * HTTP URI 설계 - Form
     * 버킷 등록 페이지                : /admin/bucket/add                 GET - 컨트롤 URI 형태
     * 버킷 수정 페이지                : /admin/bucket/{bucketId}          GET
     */

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
