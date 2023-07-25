package com.mybucketpet.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResult {
    private String code;    // 에러 코드
    private String message; // 메시지

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
