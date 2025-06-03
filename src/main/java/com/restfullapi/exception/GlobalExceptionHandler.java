package com.restfullapi.exception;

import com.restfullapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>handleNotFound(ResourceNotFoundException ex){
        ApiResponse<Object> response=new ApiResponse<>(HttpStatus.NOT_FOUND.value(),"Not Found",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
