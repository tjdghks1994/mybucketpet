package com.mybucketpet.exception.bucket;

public class BucketException extends RuntimeException {

    public BucketException(String message, Throwable cause) {
        super(message, cause);
    }
    public BucketException(String message) {
        super(message);
    }
}
