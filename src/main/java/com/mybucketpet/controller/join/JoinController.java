package com.mybucketpet.controller.join;

import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.service.login.EmailService;
import com.mybucketpet.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final EmailService emailService;
    private final MemberService memberService;

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
            bindingResult.rejectValue("joinPasswordCheck", "NotSame");
        }

        // 닉네임이 이미 존재하는 경우
        String findNickname = memberService.findByNickname(joinForm.getJoinNickname());
        if (StringUtils.hasText(findNickname)) {
            bindingResult.rejectValue("joinNickname", "alreadyExist");
        }

        // 검증 오류가 존재한다면
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "join/joinMemberForm";
        }

        Member saveMember = memberService.save(joinForm);
        log.info("saveMember = {}", saveMember);

        return "login/loginForm";
    }

    @ResponseBody
    @PostMapping("/mail/auth")
    public String mailAuthCheck(@RequestBody String email) {
        log.info("mailAuthCheck Start EMAIL = {}", email);
        // 인증번호 생성 후 메일 전송 - 반환값은 생성한 인증번호
        String authCode = emailService.sendAuthCode(email);
        log.info("LoginController authCode = {}", authCode);

        return authCode;
    }

    @ResponseBody
    @GetMapping("/mail/{email}")
    public Member mailCheck(@PathVariable String email) {
        log.info("mailCheck Start EMAIL = {}", email);
        // 전달받은 이메일이 존재하는지 조회
        return memberService.findBySameId(email);
    }

    @ResponseBody
    @GetMapping("/{nickname}")
    public String nicknameCheck(@PathVariable String nickname) {
        log.info("nicknameCheck Start nickname = {}", nickname);
        // 전달받은 닉네임이 존재하는지 조회
        return memberService.findByNickname(nickname);
    }
}
