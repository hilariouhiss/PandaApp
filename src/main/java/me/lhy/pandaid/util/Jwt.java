package me.lhy.pandaid.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Log4j2
public class Jwt {

    // 访问过期时间
    private static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(1);
    // 加密算法 HS512
    private static final MacAlgorithm MAC_ALGORITHM = Jwts.SIG.HS512;
    // 密钥，应用启动时随机生成
    private static final SecretKey SECRET_KEY = MAC_ALGORITHM.key().build();
    // jwt 签发者
    private static final String ISSUER = Constants.JWT_ISSUER;

    private static String generateToken(String username) {
        // 计算过期时间
        Instant accessTokenExpireTime = Instant.now().plus(ACCESS_TOKEN_EXPIRATION);
        var expiration = Date.from(accessTokenExpireTime);

        log.info("生成 {} 的 accessToken", username);
        return Jwts.builder()
                   .id(UUID.randomUUID().toString())  // JWT ID
                   .issuer(ISSUER)  // 签发者
                   .issuedAt(new Date())  // 签发时间
                   .expiration(expiration)  // 过期时间
                   .subject(username)  // 拥有者
                   .signWith(SECRET_KEY, MAC_ALGORITHM)
                   .compact();
    }

    public static Claims parseToken(String jws) {
        try {
            log.info("解析 token: {}", jws);
            return Jwts.parser()
                       .verifyWith(SECRET_KEY)  // 验证
                       .build()
                       .parseSignedClaims(jws)  // 解析
                       .getPayload();
        } catch (JwtException jwtException) {
            log.info("解析 token 失败: {}", jwtException.getMessage());
            return null;
        }
    }

    public static Boolean validateToken(String jws) {
        try {
            Claims claims = parseToken(jws);
            // 如果 claims 不为空, 并且未过期, 返回 true, 否则返回 false
            return claims != null && claims.getExpiration().after(new Date());
        } catch (JwtException jwtException) {
            return false;
        }
    }
}
