package com.sparta.icy.controller;

import com.sparta.icy.dto.BoardDto;
import com.sparta.icy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 게시물 관련 API를 처리하는 컨트롤러 클래스
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    // BoardService 의존성 주입을 위한 생성자
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시물을 생성
    @PostMapping
    public void createBoard(@RequestBody BoardDto boardDto) {
        boardService.createBoard(boardDto);
    }

    // 특정 게시물을 조회
    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 특정 게시물을 수정
    @PutMapping("/{id}")
    public void updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {
        boardService.updateBoard(id, boardDto);
    }

    // 특정 게시물을 삭제
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }

    // 모든 게시물을 조회
    @GetMapping
    public ResponseEntity<?> getAllBoards() {
        List<BoardDto> boards = boardService.getAllBoards();
        if (boards.isEmpty()) {
            return ResponseEntity.ok("먼저 작성하여 소식을 알려보세요!");
        } else {
            return ResponseEntity.ok(boards);
        }
    }
}