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
import org.apache.coyote.http11.filters.BufferedInputFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
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
    public String insertBoard(Board board, @Nullable@RequestParam("uploadfile") MultipartFile[] uploadfile) {
        //@Nullable@RequestParam MultipartFile[] uploadfile
        //MultipartFile[]를 클라이언트에서 받아오고, 데이터가 없더라도 허용(@Nullable)

        try {
            //boardService.insertBoard 메서드에서는 DB에 데이터를 저장하고 저정된 board_seq 리턴 받음.
            Long board_seq = boardService.insertBoard(board);

            List<FileUploadEntity> list = new ArrayList<>();
            for(MultipartFile file : uploadfile){

                //MultipartFile로 클라이언트에서 온 데이터가 무결성 조건에 성립을 안하거나 메타데이터가 없거나 문제가 새길 여지를 if문으로 처리
                if(!file.isEmpty()){
                    FileUploadEntity entity = new FileUploadEntity(null,
                            UUID.randomUUID().toString(),
                            file.getContentType(),
                            file.getName(),
                            file.getOriginalFilename(),
                            board_seq
                    );
                    //file업로드 테이블에 데이터 저장
                    boardService.insertFileUploadEntity(entity);
                    list.add(entity);
                    File newFileName = new File(entity.getUuid()+"_"+entity.getOriginalFileName());
                    //서버에 이미지 파일 저장
                    file.transferTo(newFileName);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        board.setCreateDate(new Date());

        //클라이언트에서 board객체를 받아서 매개변수로 사용
        //[1]BoardService의 inserBoard메서드 실행 >
        //[2]BoardRepository(CrudRepository).save(board)를 통해서 (JPA번역)
        //DB의 저장 (SQL)
        //insertBoard라는 메서드에 board객체
        return "redirect:getBoardList";
    }

    @GetMapping("/getBoard")
    public String getBoard(Board board, Model model) {
        FileUploadEntity fileUploadEntity = boardService.getFileuploadEntity2(board.getSeq());
        String path = "/viewImage/" + fileUploadEntity.getUuid()+"_"+fileUploadEntity.getOriginalFileName();

//        model.addAttribute("boaFrdPrv" , boardService.get);
        model.addAttribute("board", boardService.getBoard(board));
        model.addAttribute("imgLoading", path);
//        model.addAttribute("imgLoading", path+"/filer");

        return "getBoard";
    }

//    @GetMapping(value ="/image/@{imgename}", produces = MediaType.IMAGE_PNG_VALUE)

    @GetMapping(value = "/image/@{imagename}/filer", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> imageLoading(@PathVariable("imgname") String imgname) throws IOException{
        // ResponseEntity<byte[]> : 메서드 리턴타입으로 이미기 데이터를 송신하기 위한 객체<바이트 배열>
        // throws IOExeception : 스트림방식으로 데이터를 전송하 ㄹ때 도중에 오류가 날 경우를 찾기 위해서 선언한 Exception

        String path ="C:\\\\Users\\\\woaak\\\\spring\\\\soyeon\\\\src\\\\main\\\\resources\\\\static\\\\upload\\\\"+imgname;
        //File을 컴퓨터가 이해하기 위해서 Stream 배열을 만들어서 작업
        //객체(데이터 저장) : String , int ,double
        //Stream 객체는 파일을 컴퓨터가 cpu에서 바로 읽어들일 수 있도록 하는 객체
        FileInputStream fis = new FileInputStream(path);
        //Buffered : CPU에서 데이터를 읽어올 때 메모리와 캐시 사이에서 CPU와의 속도 차이를 줄이기 위한 중간 저장 위치
        BufferedInputStream bis = new BufferedInputStream(fis);
        //byte배열로 전환햐여 ResponseEntity를 통해 클라리언트에게 데이터 전달
        //HTTP프로토콜은 바이트 단위(배열)로 데이토를 주고 받음
        byte[] imgByteArr = bis.readAllBytes();
        //ResposeEntity를 통해 http프로톸놀로 클라이언트에게 데이터 전송
        return new ResponseEntity<byte[]>(imgByteArr, HttpStatus.OK);
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

//    @PostMapping("/uploadFile")
//    public String uploadFile(@RequestParam("writer") String input_writer,
//            @RequestParam("uploadFile")MultipartFile[] uploadfile
//                             )  throws IOException {
//        //multipartfile을 클라이언트에서 서버로 RequestParam데이터 받아옴 name = uploadfile
//        System.out.println("test");
//        //@Slf4j Lombook라이브러리로 log데이터 찍음
//        //info / error / debug 단위가 있고 단위마다 필터링하여 정보를 수집하고 관리 가능
//        log.info("img upload session");
//        //multipartfile데이터를 수집하여 entity FileUploadEntity에 데이터 저장
//        List<FileUploadEntity> list = new ArrayList<>();
//        for(MultipartFile file : uploadfile){
//            //file.get~ 메서드는 MultiFile (이미지) 내부에 있는 메타데이터를 가져오는 메서드
//            //input_writer는 클라이언트에서 데이터를 직접 전달하는 string데이터
//            //MultipartFile file이 있을 때까지 작업 진행
//            if(!file.isEmpty()) {
//                FileUploadEntity entity = new FileUploadEntity(null,
//                        UUID.randomUUID().toString(),
//                        file.getContentType(),
//                        file.getName(),
//                        file.getOriginalFilename(),
//                        input_writer
//                        );
//                Long output = boardService.insertFileUploadEntity(entity);
//                list.add(entity);
//                File newFileName = new File(entity.getUuid()+""+entity.getOriginalFileName()+".PNG");
//                //file을 서버에 저장하는 스트림행위가 있는 서버가 성공할지 여부를 체크하므로  exception처리 필요
//                //메서드에 throws IOException 처리 = try catch처리 필요
//                file.transferTo(newFileName);
//                log.info(newFileName.toString());
//            }
//        }
//
//        return "getBoardList";
//
//    }

    //server에서 client로 이미지 전송
    //springboot에서 URL주소를 통해 이미지를 받음 InputStream을 통해 파일을 http프로토콜에 전달하여 클라이언트에게 전송



    //MultipartFile - 파일을 담는 인터페이스 (담아서 전송)
    //[] - 여러가지를 담을 경우 배열로 담아줌 >> 몇개가 올 줄 모름 > 배열

//
//    @PostMapping("/uploadFile")
//    public String uploadFile(@RequestParam("writer") String input_writer,
//                             @RequestParam("uploadFile")MultipartFile[] uploadfile) throws IOException{
//
//        List<FileUploadEntity> list = new ArrayList<>();
//        for(MultipartFile file : uploadfile){
//            if(!file.isEmpty()){
//                FileUploadEntity entity = new FileUploadEntity(null,
//                        UUID.randomUUID().toString(),
//                        file.getContentType(),
//                        file.getName(),
//                        file.getOriginalFilename(),
//                        input_writer
//                );
//                Long output = boardService.insertFileUploadEntity(entity);
//                list.add(entity);
//                File newFileName = new File(entity.getUuid()+""+entity.getOriginalFileName()+".PNG");
//                file.transferTo(newFileName);
//            }
//        }
//        return "getBoardList";
//
//
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
