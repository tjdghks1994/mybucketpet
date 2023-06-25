package com.mybucketpet.domain.member;

public enum JoinType {
    EMAIL("이메일"), NAVER("네이버"), KAKAO("카카오"), GOGGLE("구글");

    private String joinType;

    JoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getJoinType() {
        return joinType;
    }

}
