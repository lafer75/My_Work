package com.example.demowithtests.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(ListEmptyUserException.class)
    protected ResponseEntity<MyNewExceptionHandler> handleListEmptyException() {
        return new ResponseEntity<>(new MyNewExceptionHandler("No users were found"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<MyNewExceptionHandler> handleUserNotFoundException() {
        return new ResponseEntity<>(new MyNewExceptionHandler("The user does not exist"), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NonUnException.class)
    protected ResponseEntity<MyNewExceptionHandler> handleNonUniqueException() {
        return new ResponseEntity<>(new MyNewExceptionHandler("Employees by this email more than one"), HttpStatus.CONFLICT);
    }


    @Data
    @AllArgsConstructor
    private static class MyNewExceptionHandler {
        private String message;
    }
}