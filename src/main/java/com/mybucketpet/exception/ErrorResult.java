package com.mybucketpet.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResult {
    private final String message; // 메시지

    private ErrorResult(String message) {
        this.message = message;
    }

    public static ErrorResult from(String message) {
        return new ErrorResult(message);
    }

    public String getMessage() {
        return this.message;
    }
}
