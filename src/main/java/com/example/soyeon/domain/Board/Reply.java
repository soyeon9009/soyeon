package com.example.soyeon.domain.Board;

import com.example.soyeon.domain.base.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Setter
@Getter
@Entity
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(nullable = false, length = 500)
    private String content;

    @Transient //컬럼생성 후 데이터를 넣지않고 검색용도.
    private Long board_seq;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name  = "board_seq",referencedColumnName = "seq")
    private Board board;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn( referencedColumnName = "id")
//    private Member member;



}

