package com.jhkim.dividends.exception.impl;

import com.jhkim.dividends.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class FailToScrapTickerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getMessage() {
        return "크롤링에 실패하였습니다.";
    }
}
