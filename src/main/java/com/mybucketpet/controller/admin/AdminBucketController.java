package com.mybucketpet.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/bucket")
public class AdminBucketController {

    @GetMapping
    public String bucketManageForm() {
        return "admin/bucket/bucket_manage";
    }
}
