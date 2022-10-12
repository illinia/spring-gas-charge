package com.portfolio.gascharge.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    INACTIVE_USER(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;


}
