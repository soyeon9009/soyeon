package com.example.soyeon;

import com.example.soyeon.DTO.CustomDto;
import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.persistence.BoardRepository;
import com.example.soyeon.persistence.account_info.MemberRepository;
import com.example.soyeon.service.APITest.apiTest;
import com.example.soyeon.service.TextTransfer.SeleniumExam;
import com.example.soyeon.service.account_info.MemberService;
import com.example.soyeon.service.account_info.MemberServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SoyeonApplicationTests {

	@Autowired
	apiTest apiTest_1;

	@Autowired
	SeleniumExam seleniumExam;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	BoardRepository boardRepository;
	@Test
	void apiTest_2(){
		apiTest_1.resultAPI();
	}

	@Test
	@DisplayName("저장, 데이터가 잘 들어갔는지 확인")
	void contextSave() {
		//Setter로 엔티티를 생성하고 repository가 정상 작동하는지 확인
		Member member = new Member();

		//클라이언트에서 controller에 데이터를 전달하는 내용을 setter를 통해 대체
		member.setId("testTest");
		member.setPassword("12345@");
		member.setEmail("test@test");
		//member

	}

	@Test
	void testScraping() {
		seleniumExam.scraping();
	}

	@Test
	void contextLoads() {

	}

	@Test
void testNative(){
		memberRepository.findID().forEach(System.out::println);
	}

	@Test
	void testQuery(){
		memberRepository.findAll().forEach(System.out::println);
	}

//	@Test
//	void testRepository(){
//		boardRepository.findAllByMemberIdEqualsBoardWriter("id");
//	}

}
