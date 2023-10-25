package com.mybucketpet.controller.member;

import com.mybucketpet.controller.member.dto.JoinForm;
import com.mybucketpet.controller.member.dto.LoginForm;
import com.mybucketpet.controller.member.dto.ResponseJoinInfo;
import com.mybucketpet.exception.ErrorResult;
import com.mybucketpet.exception.member.MemberException;
import com.mybucketpet.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResult> memberExceptionHandler(MemberException memberException) {
        log.error(memberException.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResult.from(memberException.getMessage()));
    }

    /**
     * HTTP URI 설계 - API
     * 회원 등록                /members                        POST
     * 회원 중복 조회            /members/{memberId}/exists      GET
     * 회원 로그인               /members/login                 POST
     * 회원 비밀번호 재설정        /members/password              PATCH
     * 회원 이메일 인증번호 발송    /members/auth-send             POST
     * 회원 이메일 인증번호 확인    /members/auth-check            POST
     * 회원 닉네임 조회          /members/nicknames             GET
     */

    @PostMapping
    public ResponseEntity<String> memberJoin(@Valid @RequestBody JoinForm joinForm) {
        ResponseJoinInfo saveMember = memberService.save(joinForm);
        URI location = URI.create("/members/login");

        return ResponseEntity.created(location).body(saveMember.getMemberId());
    }

    @GetMapping("/{memberId}/exists")
    public ResponseEntity<String> memberSearch(@PathVariable String memberId) {
        memberService.findByIdDuplicate(memberId);

        return ResponseEntity.ok(memberId + "는 사용 가능한 회원 ID 입니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        String loginId = loginForm.getLoginId();
        String loginPw = loginForm.getLoginPw();
        LoginForm loginMember = memberService.findByLoginAvailability(loginId, loginPw);

        HttpSession session = request.getSession();
        session.setAttribute("memberInfo", loginMember);

        return ResponseEntity.ok(loginMember.getLoginId() + "님 로그인 성공");
    }
}
