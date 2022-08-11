package com.example.soyeon.DTO;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class CustomDto {

    @Id
    private String input_id;
    private String input_writer;
    private String input_title;
}
