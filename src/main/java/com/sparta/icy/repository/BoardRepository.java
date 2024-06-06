package com.sparta.icy.repository;

import com.sparta.icy.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Board 엔티티에 대한 JpaRepository 인터페이스
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}