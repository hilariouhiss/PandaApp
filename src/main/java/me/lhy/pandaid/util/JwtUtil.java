package me.lhy.pandaid.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    // 访问过期时间
    private static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(1);
    // 加密算法 HS512
    private static final MacAlgorithm MAC_ALGORITHM = Jwts.SIG.HS512;
    // 密钥
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(Constants.JWT_SECRET_KEY));
    // jwt 签发者
    private static final String ISSUER = Constants.JWT_ISSUER;

    public static String generateToken(UserDetails userDetails) {
        // 计算过期时间
        Instant accessTokenExpireTime = Instant.now().plus(ACCESS_TOKEN_EXPIRATION);
        var expiration = Date.from(accessTokenExpireTime);

        return Jwts.builder().id(UUID.randomUUID().toString())  // JWT ID
                   .issuer(ISSUER)  // 签发者
                   .issuedAt(new Date())  // 签发时间
                   .expiration(expiration)  // 过期时间
                   .subject(userDetails.getUsername())  // 拥有者
                   .signWith(SECRET_KEY, MAC_ALGORITHM)
                   .compact();
    }

    public static Claims parseToken(String jws) {
        try {
            return Jwts.parser()
                       .verifyWith(SECRET_KEY)  // 验证
                       .build()
                       .parseSignedClaims(jws)  // 解析
                       .getPayload();
        } catch (JwtException jwtException) {
            return null;
        }
    }

    public static Boolean validateToken(String jws, String username) {
        try {
            Claims claims = parseToken(jws);
            // 如果 claims 不为空, 并且未过期, 返回 true, 否则返回 false
            return claims != null && username.equals(claims.getSubject()) && claims.getExpiration().after(new Date());
        } catch (JwtException jwtException) {
            return false;
        }
    }
}
