package com.mybucketpet.exception.member;

public class NotMatchPasswordException extends MemberException {
    public NotMatchPasswordException(String message) {
        super(message);
    }
}
