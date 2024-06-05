package com.sparta.icy.service;

import com.sparta.icy.dto.BoardDto;

import java.util.List;

public interface BoardService {
    void createBoard(BoardDto boardDto);
    BoardDto getBoard(Long id);
    void updateBoard(Long id, BoardDto boardDto);
    void deleteBoard(Long id);
    List<BoardDto> getAllBoards();
}