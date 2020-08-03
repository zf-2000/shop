package com.fh.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.LoginException;

@RestControllerAdvice
public class GlobleHandlerException {

    @ExceptionHandler(LoginException.class)
    public ResponseCode handlerLoginException(){
        //e.printStackTrace();
        return ResponseCode.error();
    }

    @ExceptionHandler(MyException.class)
    public ResponseCode handlerMyException(Exception e){
        //e.printStackTrace();
        return ResponseCode.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseCode handlerException(Exception e){
        e.printStackTrace();
        return ResponseCode.error();
    }
}
