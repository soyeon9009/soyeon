package com.example.soyeon.domain.Scraping;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Builder      //선택적 데이터를 구분
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ScrapingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String city;
    private String covid19_confirmed;
    private String increase_num;



}
