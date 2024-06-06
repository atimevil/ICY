package com.sparta.icy.service;

import com.sparta.icy.dto.BoardDto;
import com.sparta.icy.entity.Board;
import com.sparta.icy.exception.BoardNotFoundException;
import com.sparta.icy.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시물 생성 메서드
    public void createBoard(BoardDto boardDto) {
        Board board = new Board();
        board.setContent(boardDto.getContent());
        board.setCreate_at(LocalDateTime.now());
        board.setUpdate_at(LocalDateTime.now());
        boardRepository.save(board);
    }

    // 특정 게시물 조회 메서드
    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        BoardDto boardDto = new BoardDto();
        boardDto.setFeed_id(board.getFeed_id());
        boardDto.setContent(board.getContent());
        boardDto.setCreate_at(board.getCreate_at());
        boardDto.setUpdate_at(board.getUpdate_at());
        boardDto.setUser_id(board.getUser_id());
        return boardDto;
    }

    // 게시물 수정 메서드
    public void updateBoard(Long id, BoardDto boardDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        board.setContent(boardDto.getContent());
        board.setUpdate_at(LocalDateTime.now());
        boardRepository.save(board);
    }

    // 게시물 삭제 메서드
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        boardRepository.delete(board);
    }

    // 모든 게시물 조회 메서드
    public List<BoardDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();

        // 게시물을 생성일 기준으로 내림차순으로 정렬
        boards.sort(Comparator.comparing(Board::getCreate_at).reversed());

        return boards.stream()
                .map(board -> {
                    BoardDto boardDto = new BoardDto();
                    boardDto.setFeed_id(board.getFeed_id());
                    boardDto.setContent(board.getContent());
                    boardDto.setCreate_at(board.getCreate_at());
                    boardDto.setUpdate_at(board.getUpdate_at());
                    boardDto.setUser_id(board.getUser_id());
                    return boardDto;
                })
                .collect(Collectors.toList());
    }
}