package com.jhkim.dividends.exception.impl;

import com.jhkim.dividends.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String getMessage() {
        return "비밀번호를 잘못 입력하였습니다.";
    }
}
