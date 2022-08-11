package com.example.soyeon.controller;

import com.example.soyeon.DTO.CustomDto;
import com.example.soyeon.DTO.CustomStudentData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class TestController {

    //CRUD  restFullApi rest하게 API를 만들자(암묵적인 규칙)
    //Create, Read, Update, Delete
    //data를 전달하는 controller URI주소 만들기

    @ResponseBody
    @RequestMapping
    public CustomStudentData readAlldata() {
        CustomStudentData soyeon = new CustomStudentData();

        soyeon.setName("백소연");
        soyeon.setGroup(1);
        soyeon.setPosition("backend");

        return soyeon;
    }
//
//    @ResponseBody
//    @RequestMapping()
//    public CustomStudentData test1() {
//
//    }
}
