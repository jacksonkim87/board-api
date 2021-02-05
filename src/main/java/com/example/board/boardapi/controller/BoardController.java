package com.example.board.boardapi.controller;

import java.util.List;

import com.example.board.boardapi.model.Board;
import com.example.board.boardapi.model.ResultMessage;
import com.example.board.boardapi.repository.BoardRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping(value = "/totalCount")
	public Mono<Integer> totalCount() throws Exception {
		return Mono.just((int) boardRepository.count());
	}

	
	@GetMapping()
	public  Flux<Board>  list(@RequestParam(value="size", defaultValue = "10")  int size , @RequestParam(value="page", defaultValue = "1") int page) throws Exception {
		
		PageRequest pageRequest = PageRequest.of((page - 1), size, Sort.Direction.DESC, "num");
		Page<Board> pageList = boardRepository.findAll(pageRequest);
		List<Board> boardList = pageList.getContent();
		return Flux.fromIterable(boardList);

	}

	@GetMapping("/{num}")
	public Mono<Board> view(@PathVariable(value = "num") int num) throws Exception {
		
		Board board = boardRepository.findById(num).orElse(new Board());
		return Mono.just(board);

	}

	@PostMapping()
	public Mono<ResponseEntity<ResultMessage>> insert( @RequestBody  Mono<Board> paramBoard) throws Exception {
		
		Mono<Board> result = paramBoard.map(b -> boardRepository.save(b));
		
		return result.map(board ->{		
			return getResponseEntity(board);
		});
	}

	@PutMapping()
	public Mono<ResponseEntity<ResultMessage>> update( @RequestBody Mono<Board> paramBoard) throws Exception {
		log.debug("--------api update");
		Mono<Board> result = paramBoard.map(board -> boardRepository.saveAndFlush(board));
		return  result.map( board -> {
			return getResponseEntity(board);
		});
		

	}



	@DeleteMapping("/{num}")
	public Mono<ResponseEntity<ResultMessage>> delete(@PathVariable(value = "num") int num) throws Exception {
		boardRepository.deleteById(num);
		return Mono.just(ResponseEntity.ok(new ResultMessage("Y", "정상")));

	}


	@GetMapping("/version")
	public Mono<String> version(){
		return Mono.just("version: 0.0.10!!!!!");
	}

	private ResponseEntity<ResultMessage> getResponseEntity(Board  board) {
		if(board.getNum() > 0) {				 
			return ResponseEntity.ok(new ResultMessage("Y", "정상"));
		}			
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResultMessage("N", "오류"));	
	}

    
}
