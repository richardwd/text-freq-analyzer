package com.dwag1n.app.config.security;

import com.dwag1n.app.exceptions.dto.ResultResponse;
import com.dwag1n.app.exceptions.enums.impl.ExceptionEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

/**
 * @author: Duo Wang
 * @version: v1.0
 * If you want to handle invalid or incorrect tokens (Token) in Spring Security, you can do so by configuring a custom AuthenticationEntryPoint.
 * AuthenticationEntryPoint handles authentication errors and sends the appropriate response to the client.
 * Note: This exception cannot be intercepted by @ ExceptionHandler because it is thrown in the filter, not in the controller.
 *      @ExceptionHandler is typically used to handle exceptions thrown in the controller.
 */
@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResultResponse resultResponse = ResultResponse.error(ExceptionEnum.UNAUTHORIZED.getResultCode(), "Unauthorized: " + authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(resultResponse.toString());
    }
}
