package com.example.sample.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class AgodaRunTimeException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AgodaRunTimeException(String code) {
        super(code);
    }
}
