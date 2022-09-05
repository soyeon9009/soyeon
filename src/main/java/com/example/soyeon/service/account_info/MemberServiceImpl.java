package com.example.soyeon.service.account_info;

import com.example.soyeon.DTO.CustomDto;
import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.persistence.Custom.CustomDtoRepository;
import com.example.soyeon.persistence.account_info.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    //기능을 실행하려명 jpa를 명시해주는 부분이 있어야함.
    //서비스와 레파지토리가 연결됨.
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CustomDtoRepository customDtoRepository;


    //memberRepository : @Autowired MemberRepository를 통해 기능 실행
    //모든 회원의 정보를 가져다 오는 것.
    //return은 List<Member> : 모든 회원의 정보를 List배열에 담아서 return
    //List<Member> : 이 메서드가 실행되면 return되는 타입
    @Override
    public List<Member> getMemberList() {
        return (List<Member>) memberRepository.findAll();
        //findAll() --> memberRepository에 있는 모든정보를 가져오는 메서드 실행.
        //(List<Member>) --->형변환
    }

    //회원 1명의 정보를 Entity에 맞춰서 DB에 저장
    @Override
    public void insertMember(Member member) {
        memberRepository.save(member);

    }

    @Override
    public Member getMember(Member member) {

        return memberRepository.findById(member.getSeq()).get();   //get() -->튜플 한줄을 가져오겠다.
    }

    @Override
    public void updateMember(Member member) {
        //1.seq를 통해서 튜플 정보를 모두 가죠오기
        //2.가져온 튜플 정보 중 수정할 내용 적용
        //3.DB에 저장(덮어쓰기)
        //findById().get() : 고유키 기준으로 튜플 전체 데이터 가져오기
        Member updatemember =memberRepository.findById(member.getSeq()).get();
        //튜플 전체 내용 중에 ID/email주소 수정(setter)

        updatemember.setPassword(member.getPassword());
        updatemember.setEmail(member.getEmail());
        //crudRepository의 save 메서드를 통해 데이터 저장.
        memberRepository.save(updatemember);

        //고유키
        //1.튜플을 식별 할 수 있는 값(데이터 한 줄)
        //2.다른 테이블의 튜플과 연동하기 윟한 값(JOIN, 외랴키) : DB괁점
        //3.객체지향 방법으로 DB저장
        //3-1. 영속성: DB에 영구 저장
        //3-2. 고힙성: 다른 트렌잭션 작업이 연관되지 않도록 해주는 것
        //3-2.관리자1은 seq 10의 회원정보를 바꿨습니다. 이미 접속해 있던 관리자2가 seq10 회원의 정보를 조회하고 수정.
        //관리자1의 트랜잭션 작업이 롼료될 떄까찌 관리사2의 seq10회원정보는 엣날 정보를 조회하고 있고,
        //관리자2의 트랜잭션 자겅ㅂ이 완료되는 순간까지 관리자2는 seq10회원정보를 수정 할 수 있다.
        //다른 필드값은 수정이 가능해도, seq는 튜플의 식별자로서 수정이 불가해야, 관리자1,2의 트랜잭션 작업을 스프링부트에서 구분 할 수 있다.
//        엔티티에 고유값이 있어야 스프링부트가 이해햐고 구분할 수 있다.

    }

    @Override
    public void deleteMember(Member member) {

        memberRepository.deleteById(member.getSeq());

    }

    @Override
    public List<Member> getMemberfindIdOrEmail(String Email, String Id) {
        return memberRepository.findMemberByEmailOrId(Email,Id);
    }

//    @Override
//    public List<Member> getMemberSearchEmail(String Email) {
//        return null;
//    }

//    @Override
//    public Member getMemberfindId(String Id) {
//        return memberRepository.findFirstById(Id);
//    }

//    @Override
//    public List<Member> getMemberSearchEmail(String Email) {
//        return memberRepository.findByEmailContains(Email);
//    }

    @Override
    public List<Member> getMemberfindEmail(String Email) {
        return memberRepository.findMemberByEmail(Email);
    }

    @Override
    public List<Member> getMemberBlindEmail(Member member) {
        List<Member> blindList = memberRepository.findAll();
        for (Member blind : blindList){
            blind.setEmail(blind.getEmail().replaceAll("(?<=.{3}).","*"));
        }
        return blindList;
    }

    @Override
    public boolean booleanSearchUserEmail(Member member,String email) {
        return member.getId().equals(email);
    }

    @Override
    public boolean booleanSearchUserId(Member member,String id) {
        return member.getId().equals(id);
    }

    @Override
    public boolean booleanSearchUserPW(Member member,String password) {
        return member.getPassword().equals(password);
    }

    @Override
    public Member getExactEmail(Member member, String id, String pw) {
        Member exact = memberRepository.findMemberByIdAndPassword(id, pw);
        //        }
        //        else{
        //            return null;
        //        }
        return exact;
    }

    @Override
    public CustomDto getCustomDtoByMemberId(String memberId) {
        return customDtoRepository.findExample(memberId);
    }



}
