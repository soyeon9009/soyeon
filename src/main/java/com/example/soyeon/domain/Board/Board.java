package com.example.soyeon.domain.Board;

/**
 * @package : com.example.demo.domain
 * @name : Board.java
 * @date : 2022-08-08 오후 6:18
 * @author : Rubisco
 * @version : 1.0.0
 * @modifyed :
 * @description : 게시판 도메인
 **/

import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.domain.base.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity 이 class가 JPA통해 데이터베이스 테이블로 쓰겠다고 명시 해주는 속성
@Getter
@ToString
@Entity
@Setter
public class Board extends BaseTimeEntity {

    //@Id : PK (primary key) SQL문의 기본키
    //@GeneratedValue 자동생성 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //시퀀스 번호 차례대로
    private Long seq;

    //@Column은 title 필드값을 컬럼화할 때 길이와 null 입력 가능여부 옵션
    @Column(length = 40, nullable = false)
    private String title;

    @Column(nullable = false, updatable = false)
    private String writer;

//    @OneToMany(mappedBy ="board")
//    private List<Reply> ReplyList = new ArrayList<>();

    //@ColumnDefault 생성할 때 기본 데이터
    @Column(nullable = false)
    @ColumnDefault("'no content'")
    private String content;

    //@ManyToOne 다양한 board는 1개의 memeber를 바라본다.
    //member필드를 선언
    //참조키가 어디인지 선언(member 기본키가 board의 참조키로 기본적으로 할당)
    //board의 writer는 memeber의 id와 연관되어 있고, 참조키로 id로 연결되어 있다.


//    @ManyToOne
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private Member member;


    //타입이 날짜
//    @Temporal(TemporalType.DATE)
//    private Date createDate;

    @ColumnDefault("0")
    @Column(insertable = false, updatable = false)
    private Long cnt;

}
