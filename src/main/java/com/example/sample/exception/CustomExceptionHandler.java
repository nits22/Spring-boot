package com.example.sample.exception;

import com.example.demo.library.Utility;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class })
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        BindingResult bindingResult = ex.getBindingResult();
        String message = getError(bindingResult);
        return new ResponseEntity<>(Utility.failureResponse(message), HttpStatus.BAD_REQUEST);
    }

    private String getError(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                String msg = fieldError.getField() + " : " + fieldError.getDefaultMessage() + ", ";
                stringBuilder.append(msg);
            });
        }
        return stringBuilder.toString();
    }

    /*@ExceptionHandler({Exception.class})
    public final ResponseEntity handleAllExceptions(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleException(DataIntegrityViolationException e) {
        return new ResponseEntity<>(Utility.failureResponse("Data Integrity Violation"), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleException(NullPointerException e) {
        return new ResponseEntity<>(Utility.failureResponse("User with Email ID doesn't exist"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AgodaRunTimeException.class)
    public ResponseEntity handleException(AgodaRunTimeException e) {
        return new ResponseEntity<>(Utility.failureResponse(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity handleException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(Utility.failureResponse(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleException(NoHandlerFoundException ex) {
        return new ResponseEntity<>(Utility.failureResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleException(Exception ex) {
        return new ResponseEntity<>(Utility.failureResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
