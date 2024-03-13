package com.jhkim.dividends.exception.impl;

import com.jhkim.dividends.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoUserIdException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NO_CONTENT.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 유저 ID입니다.";
    }
}
