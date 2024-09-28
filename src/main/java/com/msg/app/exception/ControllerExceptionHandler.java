package com.msg.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.print.PrintException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    private void printException(Exception e){
        log.error("Exception:{} , message:{}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleException(Exception e) {
        printException(e);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
    }
}
