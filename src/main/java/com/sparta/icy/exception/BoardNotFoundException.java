package com.sparta.icy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP 상태 코드 404(Not Found)로 응답하는 BoardNotFoundException 예외 클래스
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends RuntimeException {

    // 예외 메시지를 전달받아 부모 클래스의 생성자를 호출하는 생성자
    public BoardNotFoundException(String message) {
        super(message);
    }
}