package com.mybucketpet.exception.bucket;

public class InvalidRecommendException extends BucketException {
    public InvalidRecommendException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRecommendException(String message) {
        super(message);
    }
}
