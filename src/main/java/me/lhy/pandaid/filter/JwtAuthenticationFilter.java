package me.lhy.pandaid.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Jwt;
import me.lhy.pandaid.util.Result;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter( UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && !jwtToken.isEmpty()) {
            if (Jwt.validateToken(jwtToken)) {
                Claims claims = Jwt.parseToken(jwtToken);
                if (claims == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                String username = claims.getSubject();
                UserDetails user =  userService.loadUserByUsername(username);

                // 创建认证对象
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities());
                // 加入安全上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("用户 {} 成功登录", username);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        objectMapper.writeValueAsString(Result.fail(401, "请登录"))
                );
            }
        }
        filterChain.doFilter(request, response);
    }
}