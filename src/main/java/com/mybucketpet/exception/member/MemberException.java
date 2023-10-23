package com.mybucketpet.exception.member;

public class MemberException extends RuntimeException {
    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(String message) {
        super(message);
    }
}
