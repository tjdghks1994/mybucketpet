package com.mybucketpet.controller.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginForm() {
        return "login/loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        log.info("=== joinForm ===");
        return "login/joinMemberForm";
    }
}
