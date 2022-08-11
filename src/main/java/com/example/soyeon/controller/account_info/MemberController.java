package com.example.soyeon.controller.account_info;

import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.service.account_info.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

//[디스패쳐 서블릿]이 컨트롤러를 찾기 위해서 선언.
@Controller
@RequestMapping(path="/account")
public class MemberController {
    //서비스 기능을 사용할 수 있음.
    //컨트롤러 클래스가 실행되면 MemberService를 불로와서 주입 '당하는'것(DI)
    //@Autowired를 사용하여 MemberController는 MemberService를 주입당하겠다고 선언
    //Sptringboot는 인식함 : MemberController가 실행하려면 MemberService가 필요함 (IOC)
    //장점 : MemberController는 실행되는 시점에서 필요한 객체만 실행할 수 있는 절약
    //장점2: 이미 컨테이너에 있는 객체를 활용하여 최대한 인스턴스(객체)를 최소한 사용
    //@Autowired는 팔드 주입 방식
    //주입 방식: 필드,메서드, 생성자 (객체의 데이터)
    //필드 주입방식경우에는 2개이상 주입할 시 어떤것이 먼저 주입당하는지 모름
    //주입 당하는 A와 B가 서로 주입당할 경우에는 어떤 게 먼저 생성할지 모르는 문제
//-------------------------------------------------------------------------------------------------------------------------
    @Autowired
    private MemberService memberService;

    //EX) 일반 자바라면, 실행하는 클래스(main) 안에서 인스턴스를 만들어서 인스턴스 안에 있는 메서드를 실행(Static: 불러옴)
    //실행되는 클래스(main)이 먼저 존재하고 인스턴스를 후에 생성
    @GetMapping("index")
    public String index(){
        return "index";
    }


//-------------------------------------------------------------------------------------------------------------------------
//    //필드 주입방식은 @Autowired를 통해 컨테이너에 주입당함(할당)
//    //final은 변하지 않는 한 개(고유객체):MemberController는 안심하고 MemberService사용
//    private final MemberService memberService;
//
//    //생성자 주입 방식은 아래 생성자에 @Autowired를 통해서 컨에이너에서 주입 당함
//    //MemberController클래스의 생성자를 선언
//    //매개변수 MemberService로 받아서 위에 있는 필드값 MemberService에 할당
//    //장점 : 객체 생성 시점에서 생성자를 통해서 주입 받기 때문에 순서가 명확하다
//    @Autowired
//    protected MemberController(MemberService memberService){
//        this.memberService = memberService;
//    }
//-------------------------------------------------------------------------------------------------------------------------
    //(클라이언트가 두 분류) : 사용자 관점,
    //시스템관리관점(회원관리, 게시판관리, 컨텐츠관리) [웹 솔루션을 관리하는 오너]
    //getAccountList : 전체 회원 목록 보기 : 웹솔루션에서 웹시스템을 관리하는 관리자 기능
    //return 타입이 String이우: HTML 파일을 찾기 위해
   @GetMapping( "/getAccountList")
   public String getAccountList(Member member, Model model){
       //model : 컨트롤러에서 작업한 결과물을 HTML에 전달하기 위한 매개체
       //attribute : key/value으로 데이터를 저장하는 메서드
       //attributeName(key) : 뒤에 있는 value를 호출하기 위한(key)
       //memberService.getMemberList() : @Autowired를 선언된 MemberService 클래스를 호풀하여
       //GetMemberList()메서드 실행


//       System.out.println(memberService.getMemberBlindEmail(member));
       model.addAttribute("List",memberService.getMemberBlindEmail(member));
       List<Member> memberList = memberService.getMemberList();
       model.addAttribute("memberList", memberList);

       return "/account/getAccountList";
   }

   @GetMapping("/getAccount")
   public String getAccount(Member member, Model model){
       model.addAttribute("member", memberService.getMember(member));
       return "account/getAccount";

   }

//-------------------------------------------------------------------------------------------------------------------------

    //deleteAccount : 회원정보 삭제
    @PostMapping("/deleteAccount")
    public String deleteAccount(Member member){
       memberService.deleteMember(member);
       return "redirect:/account/getAccountList";
    }

//-------------------------------------------------------------------------------------------------------------------------
    //회원정보 수정
    @LastModifiedDate
    @PostMapping("/updateAccount")
    public String updateAccount(Member member){

        memberService.updateMember(member);


       return "redirect:/account/getAccountList";
    }

    //+백업 entity
    //회원정보가 일정 수치까지 다다르면(혹은 이벤트가 발생했을 때 ) updateAccountAll()이라는 매소드를 통해
    //기존 entity에 테이블의 정보를 모두 백업 entity에 저장.
    //CrudRdpository를 보면 인터페이스 메서드 fidAll 회원정보 모두 불러온 뒤에 SaveAll메서드로 저장


//-------------------------------------------------------------------------------------------------------------------------
@GetMapping("/insertAccount")
    public String insertAccount(){
        return "account/insertAccount";
}
    //Memeber 라는 매개변수 controller에 전달
    //Member(Entity)이고 DTO(Data Transfer Object)
    //어디선가 받거나 만든 데이터를 객체로 만드는 것 : DTO
    @PostMapping("/insertAccount")
    public String insertAccountView(Member member){
        //클라이언트에서 ID/PW만 가져옴
        //createDate
        //upDate
        //필드값의 수가 맞지 않으면 오류남 , 클라이언트에서는 id/pw만 받아오기때문에

        member.setCreateDate(new Date());
        member.setUpdateDate(new Date());
        memberService.insertMember(member);


        return "redirect:/account/getAccountList";

    }
//-------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/selectAccount")
    public String selectAccount(){
        return "account/selectAccount";
    }

    @PostMapping("/selectAccount")
    public String resultAccount(Member member , Model model){
        model.addAttribute("resultList",memberService.getMemberfindIdOrEmail(member.getEmail(), member.getId()));
        return "account/resultAccountList";
    }
//-------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/searchEmail")
    public String searchEmail(){
        return "account/searchEmail";
    }

//    @PostMapping("/searchAccount")
//    public String searchAccount(Member member, Model model){
//        model.addAttribute("resultList", memberService.getMemberSearchEmail(member.getEmail()));
//        return "account/resultAccountList";
//    }

    @PostMapping("/searchAccount")
    public String findEmail(Member member, Model model){
        model.addAttribute("resultList",memberService.getMemberfindEmail(member.getEmail()));
        model.addAttribute("List",memberService.getMemberBlindEmail(member));
        return "account/resultAccountList";
    }
//------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/selectExactAccount")
    public String exactAccount(){
        return "account/exactAccount";
    }

    @GetMapping("/exactAccount")
    public String exact() {return "account/exactAccount";}


    @PostMapping("/exactAccount")
    public String exactAccount(Member member, Model model, @RequestParam("id") String id, @RequestParam("password")  String pw){
        Member a = memberService.getExactEmail(member,id,pw);
        if(a != null) {
            model.addAttribute("result", memberService.getExactEmail(member, id, pw));
            return "account/result";
        }
//            model.addAttribute("searchUrl", "/account/exactAccount");
        else {
            model.addAttribute("message", "일치하는 정보가 없습니다.");
            model.addAttribute("Url", "/account/exactAccount");

        }
       return "account/alert";
    }


}
