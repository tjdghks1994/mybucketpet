package com.mybucketpet.controller.login;

import com.mybucketpet.domain.member.Member;
import com.mybucketpet.service.login.EmailService;
import com.mybucketpet.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping
    public String loginForm(HttpServletRequest request, Model model) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        // 회원가입 완료 후 loginForm으로 오게 된 경우 바로 로그인 처리 후 메인 페이지로 이동 처리
        if (inputFlashMap != null) {
            HttpSession session = request.getSession();
            Member member = (Member) inputFlashMap.get("member");
            member.setMemberPw("");

            session.setAttribute("memberInfo", member);

            return "redirect:/";
        }

        model.addAttribute("loginForm", new LoginForm());

        return "login/loginForm";
    }

    @PostMapping
    public String login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult) {
        String loginId = loginForm.getLoginId();
        String loginPw = loginForm.getLoginPw();
        log.info("loginId = {}", loginId);
        log.info("loginPw = {}", loginPw);
        Member loginMember = memberService.findByLoginAvailability(loginId, loginPw);
        // StringUtils.hasText() -> 값이 있을경우 true, null 이거나 공백인 경우 false 반환
        if (StringUtils.hasText(loginMember.getMemberId())) {
            // 로그인 성공 - 메인 페이지 이동
            return "redirect:/";
        }

        bindingResult.reject("loginFail", "이메일 주소나 비밀번호가 틀립니다.");

        return "login/loginForm";
    }
}
