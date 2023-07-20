package com.mybucketpet.interceptor;

import com.mybucketpet.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.debug("== 관리자 권한 체크 인터셉터 START ==");

        String requestURI = request.getRequestURI().substring(request.getRequestURI().indexOf("/admin"));
        HttpSession session = request.getSession();
        Member memberInfo = (Member) session.getAttribute("memberInfo");
        if (session == null || memberInfo == null || memberInfo.getMemberId() == null) {
            log.debug("세션이 존재하지 않습니다. 로그인 페이지로 이동 합니다");
            response.sendRedirect(request.getContextPath() + "/members/login?redirectURL=" + requestURI);
            return false;
        }

        // 로그인 정보에 ID가 존재하지 않거나 로그인 정보에 ID가 존재하는데 testAdmin이 아닌 경우 관리자 계정이 아니라 판단
        if (!memberInfo.getMemberId().equals("testAdmin")) {
            log.debug("관리자 계정이 아닙니다. 접근권한이 없다는 에러 페이지로 이동 합니다");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
