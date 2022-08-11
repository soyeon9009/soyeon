package com.example.soyeon.controller;

/**
 * @package : com.example.demo.controller
 * @name : BoardController.java
 * @date : 2022-08-08 오후 6:24
 * @author : Rubisco
 * @version : 1.0.0
 * @modifyed :
 * @description : 게시판 컨트롤러
 **/


import com.example.soyeon.domain.Board.Board;
import com.example.soyeon.domain.Board.FileUploadEntity;
import com.example.soyeon.domain.Board.Reply;
import com.example.soyeon.domain.account_info.Member;
import com.example.soyeon.persistence.Custom.CustomDtoRepository;
import com.example.soyeon.persistence.ReplyRepository;
import com.example.soyeon.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.google.common.io.ByteStreams.toByteArray;

@Slf4j
@Controller
public class BoardController {

    private final BoardService boardService;
    private final CustomDtoRepository customDtoRepository;
    private final ReplyRepository replyRepository;

    @Autowired
    public BoardController(BoardService boardService,
                           CustomDtoRepository customDtoRepository,
                           ReplyRepository replyRepository){
        this.boardService = boardService;
        this.customDtoRepository = customDtoRepository;
        this.replyRepository = replyRepository;

    }




    @GetMapping("index")
    public String index(){
        return "index";
    }

    //BoardService의 getBoardList메서드 실행 > BoardRepository(CrudRepository).findAll()를 통해서 (JPA번역)
    //DB의 데이터 불러오기(테이블전체) (SQL)
    @GetMapping("/getBoardList")
    public String getBoardList(Model model, Board board) {
        List<Board> boardList = boardService.getBoardList();

        model.addAttribute("boardList", boardList);
        return "getBoardList";
    }

    @GetMapping("/insertBoard")
    public String insertBoardView() {
        return "insertBoard";
    }

    @PostMapping("/insertBoard")
    public String insertBoard(Board board) {
        board.setCreateDate(new Date());

        //클라이언트에서 board객체를 받아서 매개변수로 사용
        //[1]BoardService의 inserBoard메서드 실행 >
        //[2]BoardRepository(CrudRepository).save(board)를 통해서 (JPA번역)
        //DB의 저장 (SQL)
        //insertBoard라는 메서드에 board객체 인자값으로 넣기
        boardService.insertBoard(board);
        return "redirect:getBoardList";
    }

    @GetMapping("/getBoard")
    public String getBoard(Board board, Model model) {
        model.addAttribute("board", boardService.getBoard(board));
        return "getBoard";
    }

    @PostMapping ("/updateBoard")
    public String updateBoard(Board board) {
        boardService.updateBoard(board);
        return "redirect:getBoard?seq="+board.getSeq();
    }

    @GetMapping("/updateBoard")
    public String updateBoardView(Board board, Model model) {
        model.addAttribute("board", boardService.getBoard(board));
        return "insertBoard";
    }

    @PostMapping("/deleteBoard")
    public String deleteBoard(Board board) {
        boardService.deleteBoard(board);
        return "redirect:getBoardList";
    }

    @GetMapping("/selectBoard")
    public String selectBoard(Member member , Model model) {
        System.out.println("-----------board select-----------");
        //board.getID()는 클라이언트에서 가져옴

        //@service에 board를 인자값으로 넣고 메서드 실행
        boardService.getBoardListByMemberId(member);
        model.addAttribute("boardList",boardService.getBoardListByMemberId(member) );

        //회원이 작성한 게시글리스트(List<Board>)
        // >> HTML에 뿌려주면 된다.(Controller에 가면 메서드가 실행되서 다른 결과물을 리턴받기 때문)
        //어느 HTML로 가느냐? = 객체지형은 재활용성이 중요한 요인 중 하나
        //HTML 중에 재사용 할만한 것을 찾고, 그 후에 새로운 만들기에 대해 고민
        //>>getBoardList
        //return 페이지 or controller mapping
        return "getBoardList";
    }
    //---------------------------------------------------------------------------------------------------------------------

    //board_Seq전달하면 전체 Reply를 불러오는 controller method
    @GetMapping("/getReplyList")
    public String getReplyList(Reply reply , Model model){
        model.addAttribute("ReplyList", boardService.getAllReply(reply));
        return "getBoard";
    }

    @PostMapping("/insertReply")
    public String insertReply(Reply reply){
        boardService.insertReply(reply);

        return "redirect:/getBoard";
    }


    //client에서 server로 이미지파알 전송(데이터 전송)
    //html form태그에 upload버튼으로 이미지 데이터 전송(MultipartFile) > Entity기준으로 데이터 정보를 전달
    //-server는 이미지 파일을 특정 폴더에 저장
    //장점 : 서버에 원본 이미지 파일을 저장하므로 필요할 때 서버에서 전달 받을 수 있음 = db에 부담감이 줄어듦
    //단점 : 다수의 서버에 이미지를 저장할 경우, 동일한 이미지 데이터 처리에 대한 이슈 발생 =>UUID(식별자)를 통해 이미지 이름을 구분 (동일한 이미지를 한곳에 저장)
    //-server는 이미지 파일을 byte코드로 db에
    //징점 : 이미지 데이터를 한 곳에 저장하고 관리
    //단점 : DB에 많은 부하가 걸림, 데이터 저장 포맷의 한계(Oracle 기준으로 Blob 단위로 저장할 때 4gb한계에 이슈)

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("uploadFile")MultipartFile[] uploadfile)  throws IOException {
        //multipartfile을 클라이언트에서 서버로 RequestParam데이터 받아옴 name = uploadfile
        System.out.println("test");
        //@Slf4j Lombook라이브러리로 log데이터 찍음
        //info / error / debug 단위가 있고 단위마다 필터링하여 정보를 수집하고 관리 가능
        log.info("img upload session");
        //multipartfile데이터를 수집하여 entity FileUploadEntity에 데이터 저장
        List<FileUploadEntity> list = new ArrayList<>();
        for(MultipartFile file : uploadfile){
            //MultipartFile file이 있을 때까지 작업 진행
            if(!file.isEmpty()) {
                FileUploadEntity dto = new FileUploadEntity(null,
                        UUID.randomUUID().toString(),
                        file.getContentType(),
                        file.getName(),
                        file.getOriginalFilename()
                        );
                list.add(dto);
                File newFileName = new File(dto.getUuid()+""+dto.getName()+".PNG");
                //file을 서버에 저장하는 스트림행위가 있는 서버가 성공할지 여부를 체크하므로  exception처리 필요
                //메서드에 throws IOException 처리 = try catch처리 필요
                file.transferTo(newFileName);
                log.info(newFileName.toString());
            }
        }

        return "getBoardList";

    }

    //server에서 client로 이미지 전송
    //springboot에서 URL주소를 통해 이미지를 받음 InputStream을 통해 파일을 http프로토콜에 전달하여 클라이언트에게 전송



    //MultipartFile - 파일을 담는 인터페이스 (담아서 전송)
    //[] - 여러가지를 담을 경우 배열로 담아줌 >> 몇개가 올 줄 모름 > 배열

//    @PostMapping("uploadFile")
//    public String uploadFile(@RequestParam("uploadFile") MultipartFile[] uploadfile){
//        List<FileUploadEntity> list = new ArrayList<>();
//        for(MultipartFile file : uploadfile){
//            if(!file.isEmpty()){
//                FileUploadEntity dto = new FileUploadEntity( null,
//                        UUID.randomUUID().toString(),
//                        file.getContentType(),
//                        file.getName(),
//                        file.getOriginalFilename()
//                );
//
//            }
//        }


//    }

    @GetMapping("/viewImage/{imgname}")
    public ResponseEntity<byte[]> viewImage(@PathVariable("imgname")String input_imgName) throws IOException {
        //ResponseEntity<byte[]> : http프로토콜을 통해서 byte데이터를 전달하는 객체, byte(소문자 = 기본타입) []배열
        //@PathVariable : URL 주소의 값을 받아옴
        String path = "C:\\\\Users\\\\woaak\\\\spring\\\\soyeon\\\\src\\\\main\\\\resources" +
                "\\\\static\\\\upload\\\\" +input_imgName;
        //데이터(이미지)를 전송하기 위한 객체로써 java에서는 항상 데이터를 스트림타입으로 전달.
        InputStream inputStream = new FileInputStream(path);
        //데이터를 잘라서 보냄 문제가 생길 경우 롤백을 해야하기 떄문에 예외처리를 해줌.
        //byte 배열로 변환
        byte[] imgByteArr = toByteArray(inputStream);
        inputStream.close();
        //ResponseEntity를 통해 http프로토콜로 클라이언트에게 데이터 전송
        return new ResponseEntity<byte[]>(imgByteArr, HttpStatus.OK);
    }





}
