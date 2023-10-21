package com.mybucketpet.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    /**
     * HTTP URI 설계 - API
     *
     * 회원 조회                /members                GET
     * 회원 등록                /members                POST
     * 회원 로그인               /members/login         POST
     * 회원 비밀번호 재설정        /members/password      PATCH
     * 회원 이메일 인증번호 발송    /members/auth-send     POST
     * 회원 이메일 인증번호 확인    /members/auth-check    POST
     * 회원 닉네임 조회          /members/nicknames     GET
     */
}
