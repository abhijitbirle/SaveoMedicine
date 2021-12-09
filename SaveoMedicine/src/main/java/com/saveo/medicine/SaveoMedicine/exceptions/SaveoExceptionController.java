package com.saveo.medicine.SaveoMedicine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class SaveoExceptionController {

    @ExceptionHandler(SaveoExceptionHandler.class)
    public ResponseEntity<String> handleSaveoException(SaveoExceptionHandler exceptionHandler) {
        return new ResponseEntity<>("Request is invalid " + exceptionHandler.getError() + " " +
                exceptionHandler.getMessage(), HttpStatus.NOT_FOUND);
    }
}
