package com.example.soyeon.persistence.account_info;

import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.domain.base.BaseTimeEntity;
import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.swing.*;
import java.util.List;

//MemberRepository는 CrudRepository를 상속받아 기능을 온전히 씀
//CrudRepository : DB에 기본적인 SQL문을 통해 소통(INSERT INTO, SELECT,UPDATE,DELETE)
public interface MemberRepository extends JpaRepository<Member,Long> {
                                            //엔티티 단위를 Member로 선언해줌.
    //Return 내용 선언, Find~변수명에 맞춰서 메서드 생성, 필요한 매개변수\
    //Member엔티티의
    @Query(value = "select m from  Member m where m.email = :email_1 or m.id = :id_1")
    List<Member> findMemberByEmailOrId(String email_1, String id_1);

    //(ID는 중복가능한 구조에서 )Id값을 매개변수로 넣고, 아이디 생성날짜가 가장 최신인 것.
//    @Query(value = "select m from  Member m where m.id = :id order by m.createDate DESC")
//    Member findFirstById(String id);

    //JPA는 메서드이름으로 DB에 조회하는 기능
    //JPQL : JPA 통해 JPA에서 제공하는 쿼리문으로 조회(단, 엔티티 기준으로만 조회가능)
    //NativeQuery : 일반 SQL문으로 DB조회하며 보통 DTD단위로 리턴(Entity단위로 리턴X)
    //JPQL containing (SQL의 Like처럼 유사한 단어를 찾는 메서드명 )
    List<Member> findByEmailContains(String email);
    @Query(value = "select m from Member m where m.email like concat('%',:email,'%')")
    List<Member> findMemberByEmail(String email);


//    @Query(value = "select m from Member m where m.id = :id and m.email= :email")
//    List<Member> findMemberByidAndEmail(String id, String email);

    @Query(value = "select m from Member m order by m.seq asc ")
    List<Member> findAll();


    @Query(value ="select m from Member m where m.id = :Id and m.password = :pw" )
    Member findMemberByIdAndPassword(String Id, String pw);



}