package com.sparta.icy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 권한이 없는 사용자가 특정 작업을 시도할 때 발생하는 예외 클래스
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedUserException extends RuntimeException {

    // 주어진 메시지를 사용하여 UnauthorizedUserException을 생성하는 생성자
    public UnauthorizedUserException(String message) {
        super(message);
    }
}