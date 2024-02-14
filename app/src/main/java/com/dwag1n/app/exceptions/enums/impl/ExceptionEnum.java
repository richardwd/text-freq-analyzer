package com.dwag1n.app.exceptions.enums.impl;

import com.dwag1n.app.exceptions.enums.BaseErrorInfoInterface;

/**
 * @description: Exception Handling Enumeration Class
 * @author: Duo Wang
 * @version: v1.0
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {

    // Data Operation Error Definition
    SUCCESS("200", "success!"),
    BODY_NOT_MATCH("400","The requested data format does not match!"),
    UNAUTHORIZED("401","The requested digital signature does not match!"),
    FORBIDDEN("403","do not have permission!"),
    NOT_FOUND("404", "The resource was not found!"),
    INTERNAL_SERVER_ERROR("500", "server internal error!"),
    SERVER_BUSY("503","Server is busy, please try again later!"),
    S3_CLIENT_ERROR("1000", "S3 Client Error!"),
    ILLEGAL_ARGUMENT("2001", "Parameters are not legal!"),
    NO_SUCH_FILE_IN_S3("2002", "The file does not exist in S3!");

    /**
     * error code
     */
    private final String resultCode;

    /**
     * error description
     */
    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}