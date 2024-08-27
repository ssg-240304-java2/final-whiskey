package com.whiskey.rvcom.Member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * 전역 예외 처리를 위한 핸들러 클래스입니다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BadRequestException을 처리합니다.
     *
     * @param ex      발생한 예외
     * @param request 웹 요청 정보
     * @return 에러 메시지와 HTTP 상태 코드 400 (BAD_REQUEST)
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * NotFoundException을 처리합니다.
     *
     * @param ex      발생한 예외
     * @param request 웹 요청 정보
     * @return 에러 메시지와 HTTP 상태 코드 404 (NOT_FOUND)
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * 그 외의 모든 예외를 처리합니다.
     *
     * @param ex      발생한 예외
     * @param request 웹 요청 정보
     * @return 에러 메시지와 HTTP 상태 코드 500 (INTERNAL_SERVER_ERROR)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
