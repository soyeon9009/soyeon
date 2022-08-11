package com.example.soyeon;

import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.service.account_info.MemberService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing
//Entity의 @CreateDAte, #LastModifiedDate를 자동으로 Date값을 주입함.
//왜 사용하는가?
//1.클라이언트에서 : 사용자가 임의로 수정할 수 있는 위험
//이미 클라이언트에서 DATE정보를 전달 받으면 쉽게 Entity에 데이터 입력 가능
//2.서버에서 : 클라이언트가 접속하는 서버에 날짜 기준으로 일관성이 있음.
//서버에서 날짜 내장메서드를 실행하는 리소스 문제
//3.DB에서 : DB는 모든 정보를 출력하는 1개 뿐인 서버(날짜를 완전히 일관성있게 만들 수 있음)
//모든 백엔드가 접속하기 때문에 리소르 문제 야기할 가능성 높음.
@EnableJpaAuditing
@SpringBootApplication
public class SoyeonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoyeonApplication.class, args);
	}

}

