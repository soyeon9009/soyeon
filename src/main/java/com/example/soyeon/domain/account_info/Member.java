package com.example.soyeon.domain.account_info;

import com.example.soyeon.domain.base.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity
public class Member extends BaseTimeEntity implements Serializable {

    //*영속화
    //jVM밖에서도 객체를 (영원히)저장
    //COMMIT,flush,persist를 포괄하는 내용
    //SQL(MyBatis)는 DB에 맞춘 mapper라고 정의한다면,
    //JPA는 객체(Entity가,튜플)단위로 데이터베이스에 저장하는 개념을 영속화 정의

    // *IDENTITY : DB에 필드값을 저장 후에 기본키를 생성함
    // AUTO_INCREMENT처럼 DB에 값을 저장하고 나서야 기본키를 구할 수 있을 때 쓰임
    // =DB에 클라이언트에서 받은 장보를 저장 후에 기본키(ex)seq)를 부여
    // Entity가 영속상태가 되기 위해서는 식별자가 필수
    // em: EntityManger
    // em.persiste()를 하는 하는 즉시 insert SQL(기본키저장)이 DB에 전달
    // 필드값을 테이블에 저장함과 동시에 기본키 생성해서 집어 넣는다.
    //트랜젝션이 지원하는 쓰기 지연이 동작X

    // *Sequence : DB(Oracle) Sequence 함수 기능을 활용하여 생성
    // DB마다 index를 생성하고 관리하는 함수가 있음(DB에서 관리)
    // 시퀀스 전략은 em.persiste()를 호출할 때 먼저 DB시퀀스를 사용해서 식별자를 조회
    // 이후 트랜잭션 커밋 시점에 플러시가 밸생하면 엔티티를 DB에 저장

    // *Table : Seq(시퀀스)를 정보를 갖고 있는 테이블을 만들고, seq컬럼값을 저장뒤에 불러온다.
    // 여타 위에 전력과 임의의 seq table을 만들기 때문에 table 성능이 좋지 않을경우(튜닝X)
    // 속도적인 문제를 야기할 수 있다.

    // Sequence 최적화 전략
    // allocationSize 시퀀스 접근하는 횟수를 줄이기 위한 방법.
    // 예를들어, allocationSize가 50이라면, 시퀀스 함수 한번 조회시 50씩 증가하고,1~50사이에서는 메모리에서 식별자를 할당
    // 백엔드(서버)마다 DB를 조회해서 여러 서버가 동시에 접근하고 시퀀스 함수를 사용하여 시퀀스를 할당할 때
    // 1단위로 size가 증가하면EB저장에 문제를 야기할 수 있으므로, size를 넓께 잡아 메모리가 알아소 접속한 서버마다 할당해주는 전략
    // ~50으로 시퀀스 삾을 선정하므로 여러 JVM(Spring boot서버)가 동시에 동작해도 기본 키 값이 충돌하지 않는 장점, DB 부하를 피할 수 있다.


    //SELECT [*컬럼명 = 객체의필드] FROM TABLE_NAME*객체;
    //  CREATE TABLE(
    //      seq NUMBER PRIMARY KEY,
    //        id VARCHAR(40) NOT NULL
    //  )
    //JPA : 객체에 맞춰서 SQL문으로 바꿔주는 것(번역)
    //table을 만들 때, 테이블의 튜플(row)를 식별할 수 있는 기본키
    @Id
    @GeneratedValue    //자동생성
    private Long seq;

    //table 끼리 조인을 하는 조건
    //1.Member.id = member의 튜플마다 유일한 값(유니크 키)
    //member마다 게시글(board)를 쓸 수 있다는 조건이 있으므로 board입장에서는 member의 아이디가 유일해야 식별 할 수 있다.
    //2,null처리 (null이 들어가면 id를 식별 할 수 없다.)
    @Column(length = 40, nullable = false, unique = true)
    private String id;

    //member는 여러개의 board를 가질 수 있다고 선언,
    //board들을 가지고 있다고 필드에 넣음.(JPA는 이 필드내용으로 테이블 연관관계(JOIN)으로 식별)
    //@OneToMany는 member 1튜플마다 여러개의 board를 가진다는 속성 선언과 다수 엔티티 연동에
    //Springboot는 Serializable  상속 요구함.
//    @OneToMany(mappedBy ="member")
//    private List<Board> boardList = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<Reply> RelyList = new ArrayList<>();

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

//    @Setter
//    @Temporal(TemporalType.DATE)
//    private Date createDate;
//
//    @Setter
//    @Temporal(TemporalType.DATE)
//    private Date updateDate;

    //deleteYn

}


//데이터 베이스에 null값이 들어갔던 이유 : 생성자를 만들지 않아서
//@AllArgsConstructor : 모든 매개변수를 갖는 생성자
//@NaArgsConstructor : 매개변수 없는 생성자
//@Builder
//혹은 @Setter를 추가
