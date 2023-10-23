package com.mybucketpet.controller.member.dto;

import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.domain.member.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseJoinInfo {
    private final String memberId;
    private final String memberNickname;
    private final String suspendYN;
    private final JoinType joinType;
    private final MemberType memberType;

    @Builder
    public ResponseJoinInfo(Member member) {
        this.memberId = member.getMemberId();
        this.memberNickname = member.getMemberNickname();
        this.suspendYN = member.getSuspendYN();
        this.joinType = member.getJoinType();
        this.memberType = member.getMemberType();
    }
}
