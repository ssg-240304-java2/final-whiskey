package com.whiskey.rvcom.Member.exception;

/**
 * 리소스를 찾을 수 없는 예외를 나타내는 클래스입니다.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
