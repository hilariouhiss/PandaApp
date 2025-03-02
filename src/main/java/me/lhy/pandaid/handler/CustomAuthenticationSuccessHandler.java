package me.lhy.pandaid.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.lhy.pandaid.domain.dto.UserDTO;
import me.lhy.pandaid.domain.po.SecurityUser;
import me.lhy.pandaid.domain.po.User;
import me.lhy.pandaid.util.Constants;
import me.lhy.pandaid.util.Converter;
import me.lhy.pandaid.util.JwtUtil;
import me.lhy.pandaid.util.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDetailsService userDetailsService;

    public CustomAuthenticationSuccessHandler(UserDetailsService userDetailsService) {this.userDetailsService = userDetailsService;}

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        User user = (User) authentication.getPrincipal();
        String accessToken;
        try {
            accessToken = JwtUtil.generateToken(user.getUsername());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Result<Void> errorResult = Result.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResult));
            return;
        }
        // 将 JWT 添加到响应头
        response.addHeader(Constants.EXPOSURE_HEADER, accessToken);
        // 加载 SecurityUser
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(user.getUsername());
        // 取出 user 对象后转为 dto
        User user1 = securityUser.getUser();
        UserDTO dto = Converter.INSTANCE.toUserDto(user1);
        // 放入 roles
        dto.setRoles(securityUser.getRoles());
        // 返回 UserDto
        Result<UserDTO> result = Result.success(dto);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}