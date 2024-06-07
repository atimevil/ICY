package com.sparta.icy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시물 응답 DTO 클래스
@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDto {

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

    // 생성자 메서드로 각 필드의 값을 받아 초기화
    public BoardResponseDto(Long feed_id, String content, LocalDateTime create_at, LocalDateTime update_at, Long user_id) {
        this.feed_id = feed_id;
        this.content = content;
        this.create_at = create_at;
        this.update_at = update_at;
        this.user_id = user_id;
    }
}