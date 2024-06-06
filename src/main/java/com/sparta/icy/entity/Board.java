package com.sparta.icy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Board 엔티티 클래스
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "feed")
public class Board {

    // 엔티티의 기본 키를 나타내는 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long feed_id; // 게시물 고유 번호

    // 게시물의 내용을 나타내는 필드
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content; // 게시물 내용

    // 게시물의 생성일을 나타내는 필드
    @Column(name = "create_at")
    private LocalDateTime create_at; // 게시물 생성일

    // 게시물의 수정일을 나타내는 필드
    @Column(name = "update_at")
    private LocalDateTime update_at; // 게시물 수정일

    // 게시물을 작성한 사용자의 ID를 나타내는 필드
    @Column(name = "user_id")
    private Long user_id; // 사용자 고유 번호
}