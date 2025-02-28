package me.lhy.pandaid.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.util.Constants;
import me.lhy.pandaid.util.JwtUtil;
import me.lhy.pandaid.util.Result;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();



    public JwtAuthenticationFilter( UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @LogOperation("JWT认证过滤器")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(Constants.EXPOSURE_HEADER);
        if (jwtToken != null && !jwtToken.isEmpty()) {
            if (JwtUtil.validateToken(jwtToken)) {
                Claims claims = JwtUtil.parseToken(jwtToken);
                if (claims == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                String username = claims.getSubject();
                UserDetails user =  userDetailsService.loadUserByUsername(username);

                // 创建认证对象
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities());
                // 加入安全上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        objectMapper.writeValueAsString(Result.fail(HttpServletResponse.SC_UNAUTHORIZED, "请登录"))
                );
            }
        }
        filterChain.doFilter(request, response);
    }
}