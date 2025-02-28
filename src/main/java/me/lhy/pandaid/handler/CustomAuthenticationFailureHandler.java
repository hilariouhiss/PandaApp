package me.lhy.pandaid.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.lhy.pandaid.util.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(403);
        response.setContentType("application/json;charset=utf-8");

        String errorMessage = "登录失败";

        if (exception instanceof DisabledException) {
            errorMessage = "用户被禁用";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "用户名或密码错误";
        }
        Result<Void> result = Result.fail(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
