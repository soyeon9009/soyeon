package com.example.soyeon.persistence;

/**
 * @package : com.example.demo.persistence
 * @name : BoardRepository.java
 * @date : 2022-08-08 오후 6:20
 * @author : Rubisco
 * @version : 1.0.0
 * @modifyed :
 * @description : 게시판 레포지토리
 **/


import com.example.soyeon.domain.Board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//CrudRepository를 상속받음
//CrudRepository : Entity 를 매개체로 create, read, update, delete 기능을 하는 인터페이스
//CrudRepository<Board, Long>의 매개변수 Board(Entity)와 Long(PK기본키의 타입)을 선언
//JPA가 어떤 객체로 어떤 타입으로 찾아야하는지 알아 들음
public interface BoardRepository extends JpaRepository<Board, Long> {

    //튜닝 : JOIN과 WHERE절의 순서를 정함으로써 SELECT속도 튜닝을 어떻게 할지 전략적으로 구성 1>2>[3>4]
    //Member의 튜플이 무척 많을 경우 WHERE절을 통해 ID검색 이후 Board와 JOIN하는 것이 DB 검색 속도에 유리
    //회원 id를 검색하면 (writer와 Id가 동일) 관련된 writer의 게시글 모두 출력받아 리턴
    //INNER JOIN : ANSO QUERY와  ORACLE QUERY 다름
    //Board와 튜플을 가져와야 하기 때문에 FROM Board(Board 테이블이 기준)
    //(1)//SELECT b FROM Board b >>> board테이블의 튜플을 검색.(모든 컬럼)
    //(2)//INNER JOIN Member m >>>> member테이블과 교집합 조인(inner join)
    //(3)// ON b.writer = M.id >>> board의 writer와 member의 id가 동일한 튜플 검색(b는 board의 별칭,m은 member의 별핑)
    //(4)//WHERE m.id= :memberId >>> inner조인한 튜풀의 결과물 중에 member.id가 매개변수 memegerID와 동일한 조건을 걸겠다.
    @Query(value = "SELECT b FROM Board b INNER JOIN Member m ON b.writer = m.id WHERE m.id = :memberId")
    List<Board> findAllByMemberIdEqualsBoardWriter(String memberId);

//    @Query(value ="SELECT b.writer, m.id FROM Board b INNER JOIN Member m ON b.writer = m.id WHERE b.writer = :memberId",
//            nativeQuery = true)
//    Board findByWriter(String memberId);



//    @Query(value = "SELECT m.id FROM Member m INNER JOIN Board b ON m.id = b.writer WHERE m.id = :memberId", nativeQuery = true)
//    Board findByWriter(String memberId);
//

}
