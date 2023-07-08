package com.mybucketpet.controller.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping
    public String mainPage() {
        log.debug("main start");
        return "main/main";
    }
}
