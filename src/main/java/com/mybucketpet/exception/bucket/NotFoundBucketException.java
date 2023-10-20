package com.mybucketpet.exception.bucket;

public class NotFoundBucketException extends BucketException {

    public NotFoundBucketException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundBucketException(String message) {
        super(message);
    }
}
