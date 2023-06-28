package com.mybucketpet.controller.login;

import com.mybucketpet.service.login.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final EmailService emailService;

    @GetMapping
    public String loginForm() {
        return "login/loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        log.info("=== joinForm ===");
        return "login/joinMemberForm";
    }

    @ResponseBody
    @PostMapping("/join/mailAuth")
    public String mailAuthCheck(@RequestBody String email) {
        log.info("mailAuthCheck Start");
        log.info("email = {}", email);
        // 인증번호 생성 후 메일 전송 - 반환값은 생성한 인증번호
        String authCode = emailService.sendAuthCode(email);
        log.info("LoginController authCode = {}", authCode);

        return authCode;
    }
}
