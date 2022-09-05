package com.example.soyeon.service;

/**
* @package : com.example.demo.service
* @name : BoardService.java
* @date : 2022-08-08 오후 6:21
* @author : Rubisco
* @version : 1.0.0
* @modifyed : 
* @description : 게시판 서비스
**/



import com.example.soyeon.domain.Board.Board;
import com.example.soyeon.domain.Board.FileUploadEntity;
import com.example.soyeon.domain.Board.Reply;
import com.example.soyeon.domain.account_info.Member;

import java.util.List;

public interface BoardService {
    List<Board> getBoardList();

    Long insertBoard(Board board);

    Board getBoard(Board board);

    void updateBoard(Board board);

    void deleteBoard(Board board);

    void insertReply(Reply reply);

    List<Reply> getAllReply(Reply reply);

    //작성자의 모든 게시글 출력
    List<Board> getBoardListByMemberId(Member member);

    //키워드 분석
//    List<String> doNounsAnalysis(List<Board> boardList);

    Long insertFileUploadEntity(FileUploadEntity fileUploadEntity);

    FileUploadEntity getFileuploadEntity2(Long board_seq);







}
