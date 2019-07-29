package com.example.sample.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {

    private boolean status;
    private String message;
    private T data;
}
