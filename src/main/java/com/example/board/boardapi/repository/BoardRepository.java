package com.example.board.boardapi.repository;



import java.util.List;

import com.example.board.boardapi.model.Board;


/**
 * BoardRepository
 */

public interface BoardRepository {

	

	List<Board> findAll();

	Board findById(int num);

   
}