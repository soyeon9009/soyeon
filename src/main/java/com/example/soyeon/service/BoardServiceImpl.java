package com.example.soyeon.service;

/**
* @package : com.example.demo.service
* @name : BoardServiceImpl.java
* @date : 2022-08-08 오후 6:23
* @author : Rubisco
* @version : 1.0.0
* @modifyed : 
* @description : 게시판 서비스 구현체
**/

import com.example.soyeon.domain.Board.Board;
import com.example.soyeon.domain.Board.FileUploadEntity;
import com.example.soyeon.domain.Board.Reply;
import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.persistence.BoardRepository;
import com.example.soyeon.persistence.FileUploadInfoRepository;
import com.example.soyeon.persistence.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//JPA가 @Service로 선언된 클래스를 갖고 JDBC에게 기능적인 구현을 위한 속성
@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private final BoardRepository boardRepo;
    //BoardRepository에 있는 DB와 연동하여 기능하는 것을 명시
    private final ReplyRepository replyRepo;

    private final FileUploadInfoRepository fileUploadInfoRepository;

    protected BoardServiceImpl(BoardRepository boardRepo, ReplyRepository replyRepo, FileUploadInfoRepository fileUploadInfoRepository){
        this.boardRepo = boardRepo;
        this.replyRepo = replyRepo;
        this.fileUploadInfoRepository = fileUploadInfoRepository;
    }

    //클라이언트에서 받아온 Board객체의 데이터를 BoardRepository의 상속받은 CrudRepository의 findAll메서드를 통해서
    //전체 조회
    @Override
    public List<Board> getBoardList() {
        return (List<Board>) boardRepo.findAll();
    }

    //클라이언트에서 받아온 Board객체의 데이터를 BoardRepository의 상속받은 CrudRepository의 Save메서드를 통해서
    //DB에 저장 (저장하는 SQL문 만들어서 실행)
    @Override
    public Long insertBoard(Board board) {
        return boardRepo.save(board).getSeq();


    }

    @Override
    public Board getBoard(Board board) {
        return boardRepo.findById(board.getSeq()).get();
    }

    @Override
    public void updateBoard(Board board) {
        Board findBoard = boardRepo.findById(board.getSeq()).get();
        findBoard.setTitle(board.getTitle());
        findBoard.setContent(board.getContent());
        boardRepo.save(findBoard);
    }

    @Override
    public void deleteBoard(Board board) {
        boardRepo.deleteById(board.getSeq());
    }

    @Override
    public void insertReply(Reply reply) {
        Board board = new Board();
        board.setSeq(reply.getBoard_seq());
        reply.setBoard(board);

        replyRepo.save(reply);
    }

    @Override
    public List<Reply> getAllReply(Reply reply) {
        return replyRepo.findReplyByBoard_seq(reply.getBoard_seq());
    }

    @Override
    public Long insertFileUploadEntity(FileUploadEntity fileUploadEntity) {

        return fileUploadInfoRepository.save(fileUploadEntity).getId();

        //getId > 테이블의 pk값을 가져옴, 테이블의 연동성에 좋음.
        //DB에 들어간 이후에 pk값을 알 수 있음.

    }

    @Override
    public FileUploadEntity getFileuploadEntity2(Long board_seq) {
        return fileUploadInfoRepository.findByBoardSeq(board_seq);
    }

    @Override
    public List<Board> getBoardListByMemberId(Member member) {
        //Repository
        return boardRepo.findAllByMemberIdEqualsBoardWriter(member.getId());

    }

}
