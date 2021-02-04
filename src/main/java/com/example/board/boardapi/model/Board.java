package com.example.board.boardapi.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Board
 */
@Getter
@Setter
@Entity
@Table(name = "board")
public class Board {
    @Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int num;
	
	@Column
	private String title;
	
	@Column
	private String contents;
	
	@Column(name = "write_name")
	private String writeName;
	
	@Column(name = "write_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime writeDate;	 
	
	@Column(name = "modify_name")
	private String modifyName;
	
	@Column(name = "modify_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime  modifyDate;

	
	@Column(name = "write_id")	
	private String writeId;
	
	@Column(name = "modify_id")
	private String modifyId;
    
}
