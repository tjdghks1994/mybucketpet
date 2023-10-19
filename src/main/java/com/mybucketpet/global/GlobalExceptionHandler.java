package com.mybucketpet.global;

import com.mybucketpet.exception.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResult> exceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.from(exception.getMessage()));
    }

    /**
     * @Valid 어노테이션를 사용한 객체 유효성 검증 실패시 발생하는 예외를 핸들링해주기 위한 메서드 작성
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResult> validExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder errorBuilder = new StringBuilder();

        fieldErrors.stream().forEach((fieldError) -> errorBuilder.append(fieldError.getDefaultMessage()).append("\n"));
        log.error(errorBuilder.toString(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResult.from(errorBuilder.toString()));
    }
}
