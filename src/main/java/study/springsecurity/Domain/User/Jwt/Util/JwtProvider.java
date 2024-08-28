package study.springsecurity.Domain.User.Jwt.Util;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import study.springsecurity.Domain.User.Jwt.Dto.JwtDto;
import study.springsecurity.Domain.User.Jwt.Exception.SecurityCustomException;
import study.springsecurity.Domain.User.Jwt.Exception.TokenErrorCode;
import study.springsecurity.Domain.User.Jwt.UserDetail.CustomUserDetail;
import study.springsecurity.Domain.User.Jwt.UserDetail.CustomUserDetailService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.*;
import study.springsecurity.Global.Util.RedisUtil;

@Slf4j
@Component
public class JwtProvider {
    private final CustomUserDetailService customUserDetailService;
    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final RedisUtil redisUtil;

    public JwtProvider(
            CustomUserDetailService customUserDetailService,
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.token.access-expiration-time}") Long access,
            @Value("${spring.jwt.token.refresh-expiration-time}") Long refresh,
            RedisUtil redis) {
        this.customUserDetailService = customUserDetailService;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        accessExpMs = access;
        refreshExpMs = refresh;
        redisUtil = redis;
    }

    //accessToken 생성
    public String createJwtAccessToken(CustomUserDetail customUserDetails) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusMillis(accessExpMs);

        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("typ", "JWT")
                .and()
                .claim("email", customUserDetails.getUsername())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    //refreshToken 생성
    public String createJwtRefreshToken(CustomUserDetail customUserDetails) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusMillis(refreshExpMs);

        String refreshToken = Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("typ", "JWT")
                .and()
                .claim("email", customUserDetails.getUsername())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
        redisUtil.save(
                customUserDetails.getUsername(),
                refreshToken,
                refreshExpMs,
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }

    //refreshToken 검증
    public boolean validateRefreshToken(String refreshToken) {
        // refreshToken validate
        String username = getUserEmail(refreshToken);
        //redis 확인
        if (!redisUtil.hasKey(username)) {
            throw new SecurityCustomException(TokenErrorCode.INVALID_TOKEN);
        }

        return true;
    }

    public String getUserEmail(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        /*
        이 메서드는 HTTP 요청의 Authorization 헤더에서 Bearer로 시작하는 JWT를 추출한다. 요청에 토큰이 없거나 잘못된 형식이면 null을 반환하고,
        유효한 토큰이 있다면 이를 추출하여 반환한다. 이 JWT는 이후 인증 및 권한 부여 과정에서 사용된다.
         */
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.warn("[*] No Token in req");

            return null;
        }

        log.info("[*] Token exists");

        return authorization.split(" ")[1];
        //Bearer말고 실제 토큰 값을 추출한다.
    }

    public JwtDto reissueToken(String refreshToken) throws SignatureException {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(getUserEmail(refreshToken));
        return new JwtDto(
                createJwtAccessToken((CustomUserDetail) userDetails),
                createJwtRefreshToken((CustomUserDetail) userDetails));
    }

    public Long getExpTime(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                .getTime();
    }
}
