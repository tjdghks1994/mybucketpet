package com.mybucketpet.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Member {

    private Long memberNo;              // 회원번호
    private String memberId;            // 회원 아이디
    private String memberPw;            // 회원 비밀번호
    private String memberNickname;      // 회원 닉네임
    private String marketingYN;         // 마케팅 동의여부
    private String suspendYN;           // 휴면(정지)회원 여부
    private LocalDate joinDate;         // 가입일자
    private LocalDate lastAccessDate;   // 최근접속일자
    private JoinType joinPath;          // 가입경로

    public Member() { }

    public Member(String memberId, String memberPw, String memberNickname, String marketingYN, String suspendYN, JoinType joinPath) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberNickname = memberNickname;
        this.marketingYN = marketingYN;
        this.suspendYN = suspendYN;
        this.joinPath = joinPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Member)) {
            throw new IllegalArgumentException("잘못된 매개변수입니다. Member 객체를 비교하세요.");
        }

        Member member = (Member) obj;

        return memberId.equals(member.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}
