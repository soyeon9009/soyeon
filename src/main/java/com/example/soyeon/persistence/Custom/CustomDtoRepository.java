package com.example.soyeon.persistence.Custom;

import com.example.soyeon.DTO.CustomDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomDtoRepository extends JpaRepository<CustomDto , String> {

    //별칭 - DTO컬럼의 별칭
    //member기준으로 아이디와 writer가 같은지 확인

    //Dto객체를 만들어서 ?

    //nativeQuery 사용이유
    //1.Entity단위로 DB조회와 Client데이터 전송을 동시에 할 경우 table 구조가 드러나기 때문에 보안 위험 > DTO를 만들어서 데이터 전송에
    //2.JPA Entity단위로 데이터를 조회할 경우 자유도가 현격히 떨어지므로 일반 DTO를 만들어서 JOIN 등 데이터 리턴값을 자유롭게 받을 수 있음.
    //단점: JPQL을 써서 데이터를 주고 받을 경우 객체 구조적인 단단함, 유지보수에 용이
    //SQL보다는 JPQL+DTD를 쓰는게 가장 좋다고 생각: 구조적인 견고함과 DTO의 유연함들 동시에 취하고 상황에 따라 대체 할 수 있는 유연을 가짐.
    @Query(value =
            "SELECT m.id AS input_id, b.writer AS input_writer, b.title AS input_title" +
                " FROM Member m" +
                " INNER JOIN Board b ON m.id = b.writer " +
                "WHERE m.id =:memberId",
            nativeQuery = true)
        CustomDto findExample(String memberId);

}
