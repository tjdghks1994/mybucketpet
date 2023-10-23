package com.mybucketpet.domain.member;

public enum MemberType {    // 회원 유형
    ADMIN("관리자"), NORMAL("일반회원"), SUSPENDED("정지회원");

    private String memberType;

    MemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberType() {
        return memberType;
    }
}
