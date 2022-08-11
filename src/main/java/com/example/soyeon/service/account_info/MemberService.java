package com.example.soyeon.service.account_info;

import com.example.soyeon.domain.account_info.Member;

import java.util.List;

public interface MemberService {

    //Member타입으로 리턴하겠다.
    List<Member> getMemberList();

    void insertMember(Member member);

    Member getMember(Member member);

    void updateMember(Member member);

    void deleteMember(Member member);

    //일부분만 검색하여 사용유저 찾기
    //결과값: 입력받은 정보(id, password)가 사실유무 확인 후 비밀번호 변경(updateMember의 password)

    //Email또는 ID를 조회하여 튜플찾기
    List<Member> getMemberfindIdOrEmail(String Email, String Id);

//    Member getMemberfindId(String Id);

//    List<Member> getMemberSearchEmail(String Email);

    List<Member> getMemberfindEmail(String Email);

    List<Member> getMemberBlindEmail(Member member);
//
    boolean booleanSearchUserEmail(Member member, String email);

    boolean booleanSearchUserId(Member member, String Id);

    boolean booleanSearchUserPW(Member member, String password);

    Member getExactEmail(Member member, String id, String pw);



}
