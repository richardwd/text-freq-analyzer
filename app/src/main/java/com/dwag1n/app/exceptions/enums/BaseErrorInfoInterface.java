package com.dwag1n.app.exceptions.enums;

/**
 * @description: Service Interface Class
 * @author: Duo Wang
 */
public interface BaseErrorInfoInterface {

    /**
     *  错误码
     */
    String getResultCode();

    /**
     * 错误描述
     */
    String getResultMsg();
}
