package com.book.demo.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {
    // common

    // account
    ACCOUNT_SAVE_FAIL(HttpStatus.BAD_REQUEST, "UD-001","account save fail")
    , ACCOUNT_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "UD-002", "account login fail")
    , ACCOUNT_RE_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "UD-003", "account reLogin fail")
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

    Error(HttpStatus httpStatus,String errorCode, String msg) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.msg = msg;
    }
}
