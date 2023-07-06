package com.mybucketpet.controller.join;

import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.service.login.EmailService;
import com.mybucketpet.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class JoinController {
    /**
     * HTTP URI 설계
     * === HTML Form 사용 (컨트롤 URI 사용) ===
     * 회원 가입 폼 :    /members/join GET
     * 회원 가입 처리 :   /members/join POST
     * === HTTP API 사용 ===
     * 회원 메일 조회 :   /members/mail/{email}   GET
     * 회원 메일 인증번호 생성 :  /members/mail/{email}/auth POST
     * 회원 메일 인증번호 확인 :  /members/mail/{email}/auth/{authCode}   GET
     * 회원 닉네임 조회 : /members/nickname/{nickname} GET
     * */

    private final EmailService emailService;
    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new JoinForm(JoinType.EMAIL));
        return "join/joinMemberForm";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute JoinForm joinForm, BindingResult bindingResult,
                       RedirectAttributes rs) {
        log.debug("joinForm = {}", joinForm);

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
            log.debug("bindingResult = {}", bindingResult);
            return "join/joinMemberForm";
        }

        Member saveMember = memberService.save(joinForm);
        rs.addFlashAttribute("member", saveMember);
        log.debug("saveMember = {}", saveMember);

        return "redirect:login";
    }

    @ResponseBody
    @PostMapping("/mail/{email}/auth")
    public ResponseEntity<String> mailAuth(@PathVariable String email, HttpServletRequest request) {
        log.debug("mailAuth CreateToSend Start EMAIL = {}", email);
        // 인증번호 생성 후 메일 전송 - 반환값은 생성한 인증번호
        String authCode = emailService.sendAuthCode(email);
        log.debug("LoginController authCode = {}", authCode);
        HttpSession session = request.getSession();
        // 서버 세션에 key-이메일, value-인증번호로 저장 -> 추후에 인증번호 확인에 사용
        session.setAttribute(email, authCode);

        return new ResponseEntity<>("authCodeOk", HttpStatus.CREATED);
    }

    @GetMapping("/mail/{email}/auth/{authCode}")
    @ResponseBody
    public String mailAuthCheck(@PathVariable String email, @PathVariable String authCode, HttpServletRequest request) {
        log.debug("mailAuthCheck Start EMAIL = {}, AUTH_CODE = {}", email, authCode);
        HttpSession session = request.getSession();
        // 이메일을 key로 서버 세션에 저장되어 있는 인증번호 값을 꺼냄
        String saveAuthCode = (String) session.getAttribute(email);
        boolean sameAuthCode = false;
        if (StringUtils.hasText(saveAuthCode)) {    // 서버 세션에 인증번호가 존재한 경우 인증번호 확인
            sameAuthCode = saveAuthCode.equals(authCode);
        }
        // 입력된 인증번호와 서버 세션에 보관되어있는 인증번호랑 동일하면 성공
        if (sameAuthCode) {
            return "ok";
        } else {
            return "fail";
        }
    }

    @ResponseBody
    @GetMapping("/mail/{email}")
    public Member mailCheck(@PathVariable String email) {
        log.debug("mailCheck Start EMAIL = {}", email);
        // 전달받은 이메일이 존재하는지 조회
        return memberService.findById(email);
    }

    @ResponseBody
    @GetMapping("/nickname/{nickname}")
    public String nicknameCheck(@PathVariable String nickname) {
        log.debug("nicknameCheck Start nickname = {}", nickname);
        // 전달받은 닉네임이 존재하는지 조회
        return memberService.findByNickname(nickname);
    }
}
