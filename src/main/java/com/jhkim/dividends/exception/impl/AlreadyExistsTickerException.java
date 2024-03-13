package com.jhkim.dividends.exception.impl;

import com.jhkim.dividends.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsTickerException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 회사입니다.";
    }
}
