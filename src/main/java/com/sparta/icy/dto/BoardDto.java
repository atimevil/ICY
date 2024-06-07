package com.sparta.icy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시물 DTO 클래스
@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    // 게시물 고유 번호를 나타내는 필드
    private Long feed_id;

    // 게시물 내용을 나타내는 필드
    private String content;

    // 게시물 생성일을 나타내는 필드
    private LocalDateTime create_at;

    // 게시물 수정일을 나타내는 필드
    private LocalDateTime update_at;

    // 게시물을 작성한 사용자의 고유 번호를 나타내는 필드
    private Long user_id;
}