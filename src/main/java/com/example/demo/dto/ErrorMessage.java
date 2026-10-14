package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorMessage {
    private Integer statusCode;
    private LocalDate dateError;
    private String message;
    private String description;
}
