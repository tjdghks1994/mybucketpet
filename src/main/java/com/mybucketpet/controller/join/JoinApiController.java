package com.mybucketpet.controller.join;

import com.mybucketpet.domain.member.Member;
import com.mybucketpet.exception.ErrorResult;
import com.mybucketpet.service.login.EmailService;
import com.mybucketpet.service.member.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class JoinApiController {

    private final EmailService emailService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MessagingException.class)
    public ErrorResult msExceptionHandler(MessagingException me) {
        log.error("[MessagingExceptionHandler]", me);
        return new ErrorResult("SendAuthCodeError", "메일을 보내는 도중 오류가 발생했습니다!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult exceptionHandler(Exception e) {
        log.error("[ExceptionHandler]", e);
        return new ErrorResult("ServerError", "내부 오류가 발생했습니다!");
    }
    /**
     * HTTP URI 설계 - API
     * 회원 메일 조회 :           /members/mail/{email}                   GET
     * 회원 메일 인증번호 생성 :    /members/mail/{email}/auth              POST
     * 회원 메일 인증번호 확인 :    /members/mail/{email}/auth/{authCode}   GET
     * 회원 닉네임 조회 :         /members/nickname/{nickname}            GET
     */

    @PostMapping("/mail/{email}/auth")
    public ResponseEntity<String> mailAuth(@PathVariable String email, HttpServletRequest request) throws MessagingException {
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

    @GetMapping("/mail/{email}")
    public Member mailCheck(@PathVariable String email) {
        log.debug("mailCheck Start EMAIL = {}", email);
        // 전달받은 이메일이 존재하는지 조회
        return memberService.findById(email);
    }

    @GetMapping("/nickname/{nickname}")
    public String nicknameCheck(@PathVariable String nickname) {
        log.debug("nicknameCheck Start nickname = {}", nickname);
        // 전달받은 닉네임이 존재하는지 조회
        return memberService.findByNickname(nickname);
    }
}
