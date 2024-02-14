package com.dwag1n.app.exceptions.exception;

import com.dwag1n.app.exceptions.enums.BaseErrorInfoInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

/**
 * @description: Customizing Exception Classes
 * @author: Duo Wang
 * @version: v1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * error code
     */
    protected String errorCode;
    /**
     * error message
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getResultCode());
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BizException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getResultCode(), cause);
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}