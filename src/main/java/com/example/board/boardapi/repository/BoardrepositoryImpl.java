package com.example.board.boardapi.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.board.boardapi.model.Board;

import org.springframework.stereotype.Service;

@Service
public class BoardrepositoryImpl implements BoardRepository {

    @Override
    public List<Board> findAll() {
        List<Board> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(new Board(i, "제목" + i, "내용" + i, "사용자1", "20210215", null, null, "user1", null));

        }

        return list;
    }

    @Override
    public Board findById(int num) {
        Board board = new Board(num, "제목" + num, "내용" + num, "사용자1", "20210215", null, null, "user1", null);

        return board;
    }

}
