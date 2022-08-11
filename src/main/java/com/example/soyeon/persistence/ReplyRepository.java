package com.example.soyeon.persistence;

 import com.example.soyeon.domain.Board.Reply;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query(value = "SELECT r FROM Reply r JOIN FETCH r.board WHERE r.board.seq = :input_board_seq")
    List<Reply> findReplyByBoard_seq(Long input_board_seq);


    //FETCH JOIN : 조회의 주체가 되는 Entity이외에 FETCH JOIN이 걸린 연관 Entity도 함께 SELECT하여 모두 영속화화

}

