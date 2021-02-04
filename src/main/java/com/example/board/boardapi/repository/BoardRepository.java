package com.example.board.boardapi.repository;



import com.example.board.boardapi.model.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * BoardRepository
 */

public interface BoardRepository extends JpaRepository<Board, Integer>{

    @Modifying
	@Query(value="UPDATE board b set b.title = :#{#board.title} b.contents = :#{#board.contents}, b.modify_id = :#{#board.modifyId}, b.modify_name = :#{#board.modifyName} , b.modify_date = :#{#board.modifyDate} WHERE b.num = :#{#board.num}", nativeQuery= true)
	Integer updateBoard(@Param("board") Board board);
}