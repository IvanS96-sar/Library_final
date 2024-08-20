package ru.maxima.dto;

import lombok.Data;

@Data
public class BookDTO {
    private String name;
    private Integer yearOfProduction;
    private String author;
    private  String annotation;
}