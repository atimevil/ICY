package com.sparta.icy.controller;

import com.sparta.icy.dto.BoardDto;
import com.sparta.icy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public void createBoard(@RequestBody BoardDto boardDto) {
        boardService.createBoard(boardDto);
    }

    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping("/{id}")
    public void updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {
        boardService.updateBoard(id, boardDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }

    @GetMapping
    public List<BoardDto> getAllBoards() {
        return boardService.getAllBoards();
    }
}