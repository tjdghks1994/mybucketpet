package com.mybucketpet.exception.bucket;

public class NotFoundThumbnailException extends BucketException {
    public NotFoundThumbnailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundThumbnailException(String message) {
        super(message);
    }
}
