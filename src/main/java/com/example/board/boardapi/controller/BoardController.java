package com.example.board.boardapi.controller;

import java.util.List;

import com.example.board.boardapi.model.Board;
import com.example.board.boardapi.repository.BoardRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardController {
   
	private final BoardRepository boardRepository;

	
	@GetMapping()
	public  Flux<Board>  list() throws Exception {		
		
		List<Board> boardList = boardRepository.findAll();
		
		return Flux.fromIterable(boardList);

	}

	@GetMapping("/{num}")
	public Mono<Board> view(@PathVariable(value = "num") int num) throws Exception {
		
		Board board = boardRepository.findById(num);
		return Mono.just(board);

	}


	@GetMapping("/version")
	public Mono<String> version(){
		return Mono.just("version: 0.0.10!!!!!");
	}

	
    
}
