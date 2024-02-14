package com.dwag1n.app.exceptions;

import com.dwag1n.app.exceptions.dto.ResultResponse;
import com.dwag1n.app.exceptions.enums.impl.ExceptionEnum;
import com.dwag1n.app.exceptions.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.access.AccessDeniedException;

/**
 * @description: Custom Exception Handling
 * @author: Duo Wang
 * @version: v1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handling Custom Business Exceptions
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResponseEntity<ResultResponse> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("Business Exception! The reason is: {}，Request URL: {}", e.getErrorMsg(), req.getRequestURL());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResultResponse.error(e.getErrorCode(), e.getErrorMsg()));
    }

    /**
     * Handling an Exception for an Empty Pointer
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ResponseEntity<ResultResponse> exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("An empty pointer exception occurred! The reason is:",e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ResultResponse> exceptionHandler(HttpServletRequest req, AccessDeniedException e){
        log.error("Access denial exception occurs! The reason is:",e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResultResponse.error(ExceptionEnum.FORBIDDEN));
    }

    /**
     * Handling other exceptions
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResponseEntity<ResultResponse> exceptionHandler(HttpServletRequest req, Exception e){
        log.error("Unknown exception! The reason is:",e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }
}