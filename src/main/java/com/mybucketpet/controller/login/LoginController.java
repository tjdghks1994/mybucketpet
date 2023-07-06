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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class LoginController {
    /**
     * HTTP URI 설계
     * === HTTP Form 사용 (컨트롤 URI 사용) ===
     * 회원 로그인 폼 :   /members/login GET
     * 회원 로그인 처리 : /members/login  POST
     * 회원 비밀번호 재설정 폼 : /members/password    GET
     * 회원 비밀번호 재설정 처리 : /members/password   POST
     */

    private final MemberService memberService;

    @GetMapping("/login")
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

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult) {
        String loginId = loginForm.getLoginId();
        String loginPw = loginForm.getLoginPw();
        log.debug("loginId = {}", loginId);
        log.debug("loginPw = {}", loginPw);
        Member loginMember = memberService.findByLoginAvailability(loginId, loginPw);
        // StringUtils.hasText() -> 값이 있을경우 true, null 이거나 공백인 경우 false 반환
        if (StringUtils.hasText(loginMember.getMemberId())) {
            // 로그인 성공 - 메인 페이지 이동
            return "redirect:/";
        }

        bindingResult.reject("loginFail", "이메일 주소나 비밀번호가 틀립니다.");

        return "login/loginForm";
    }

    @GetMapping("/password")
    public String passwordForm(Model model) {
        model.addAttribute(new PasswordChangeForm());
        return "login/passwordForm";
    }

    @PostMapping("/password")
    public String passwordChange(@Validated @ModelAttribute PasswordChangeForm passwordChangeForm, BindingResult bindingResult) {
        log.debug("passwordChange Start passwordChangeForm = {}", passwordChangeForm);
        // 비밀번호와 비밀번호 확인의 값이 서로 다른 경우
        if (!passwordChangeForm.getChangePassword().equals(passwordChangeForm.getChangePasswordCheck())) {
            bindingResult.rejectValue("changePasswordCheck", "NotSame");
        }

        // 검증 오류가 존재한다면
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult = {}", bindingResult);
            return "login/passwordForm";
        }

        memberService.update(passwordChangeForm);

        return "redirect:password/success";
    }

    @GetMapping("/password/success")
    public String passwordSuccessForm() {
        return "login/passwordSuccessForm";
    }
}
