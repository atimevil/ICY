package com.sparta.icy.service;

import com.sparta.icy.dto.BoardDto;
import com.sparta.icy.entity.Board;
import com.sparta.icy.exception.BoardNotFoundException;
import com.sparta.icy.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createBoard(BoardDto boardDto) {
        Board board = modelMapper.map(boardDto, Board.class);
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);
    }

    @Override
    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + id));
        return modelMapper.map(board, BoardDto.class);
    }

    @Override
    public void updateBoard(Long id, BoardDto boardDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + id));

        board.setContent(boardDto.getContent());
        board.setUpdatedAt(LocalDateTime.now());

        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + id));

        boardRepository.delete(board);
    }

    @Override
    public List<BoardDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> modelMapper.map(board, BoardDto.class))
                .collect(Collectors.toList());
    }
}