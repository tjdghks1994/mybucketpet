package com.mybucketpet.exception.bucket;

public class SaveFailThumbnailException extends RuntimeException {
    public SaveFailThumbnailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveFailThumbnailException(String message) {
        super(message);
    }
}
