package com.example.demo.service;

/**
* @package : com.example.demo.service
* @name : BoardService.java
* @date : 2022-08-08 오후 6:21
* @author : Rubisco
* @version : 1.0.0
* @modifyed : 
* @description : 게시판 서비스
**/

import com.example.demo.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> getBoardList();

    void insertBoard(Board board);

    Board getBoard(Board board);

    void updateBoard(Board board);

    void deleteBoard(Board board);
}
