package com.mybucketpet.controller.join;

import com.mybucketpet.controller.join.dto.JoinForm;
import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class JoinController {
    /**
     * HTTP URI 설계 - Form
     * 회원 가입 폼   :    /members/join     GET
     * 회원 가입 처리 :    /members/join     POST
     * */

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
}
