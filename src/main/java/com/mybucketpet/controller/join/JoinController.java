package com.mybucketpet.controller.join;

import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.service.login.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final EmailService emailService;

    @GetMapping()
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new JoinForm(JoinType.EMAIL));
        return "join/joinMemberForm";
    }

    @PostMapping()
    public String join(@Validated @ModelAttribute JoinForm joinForm, BindingResult bindingResult) {
        log.info("joinForm = {}", joinForm);

        // 비밀번호와 비밀번호 확인의 값이 서로 다른 경우
        if (!joinForm.getJoinPassword().equals(joinForm.getJoinPasswordCheck())) {
            bindingResult.reject("NotSamePassword");
        }

        // 검증 오류가 존재한다면
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "join/joinMemberForm";
        }

        return "login/loginForm";
    }

    @ResponseBody
    @PostMapping("/mailAuth")
    public String mailAuthCheck(@RequestBody String email) {
        log.info("mailAuthCheck Start");
        log.info("email = {}", email);
        // 인증번호 생성 후 메일 전송 - 반환값은 생성한 인증번호
        String authCode = emailService.sendAuthCode(email);
        log.info("LoginController authCode = {}", authCode);

        return authCode;
    }
}
