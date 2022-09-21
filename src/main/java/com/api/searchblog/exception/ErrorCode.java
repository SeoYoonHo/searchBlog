package com.api.searchblog.exception;

import lombok.Getter;

public enum ErrorCode {

    NOT_NULL("ERROR_CODE_0001", "필수값이 누락되었습니다"),
    NOT_EMPTY("ERROR_CODE_0002", "필수값이 비어있습니다"),
    MIN_VALUE("ERROR_CODE_0003", "최소값보다 커야 합니다.");


    @Getter
    private final String code;

    @Getter
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}