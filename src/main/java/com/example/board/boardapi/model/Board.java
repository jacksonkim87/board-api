package com.example.board.boardapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Board
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board {

	private int num;

	private String title;

	private String contents;

	private String writeName;

	private String writeDate;

	private String modifyName;

	private String modifyDate;

	private String writeId;

	private String modifyId;

}
