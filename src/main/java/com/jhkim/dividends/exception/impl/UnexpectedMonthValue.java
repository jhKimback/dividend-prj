package com.jhkim.dividends.exception.impl;

import com.jhkim.dividends.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnexpectedMonthValue extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "지정된 월의 형태가 아닙니다.";
    }
}
