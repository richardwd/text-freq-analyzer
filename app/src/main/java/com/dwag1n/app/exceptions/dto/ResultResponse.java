package com.dwag1n.app.exceptions.dto;

import com.alibaba.fastjson.JSONObject;
import com.dwag1n.app.exceptions.enums.BaseErrorInfoInterface;
import com.dwag1n.app.exceptions.enums.impl.ExceptionEnum;
import lombok.Data;

/**
 * @description: Custom Data Transfer
 * @author: Duo Wang
 * @version: v1.0
 */
@Data
public class ResultResponse {
    /**
     * response code
     */
    private String code;

    /**
     * response message
     */
    private String message;

    /**
     * response data
     */
    private Object result;

    public ResultResponse() {
    }

    public ResultResponse(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * success
     */
    public static ResultResponse success() {
        return success(null);
    }

    /**
     * success
     */
    public static ResultResponse success(Object data) {
        ResultResponse rb = new ResultResponse();
        rb.setCode(ExceptionEnum.SUCCESS.getResultCode());
        rb.setMessage(ExceptionEnum.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }

    /**
     * failure
     */
    public static ResultResponse error(BaseErrorInfoInterface errorInfo) {
        ResultResponse rb = new ResultResponse();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * failure
     */
    public static ResultResponse error(String code, String message) {
        ResultResponse rb = new ResultResponse();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    /**
     * failure
     */
    public static ResultResponse error( String message) {
        ResultResponse rb = new ResultResponse();
        rb.setCode("-1");
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}