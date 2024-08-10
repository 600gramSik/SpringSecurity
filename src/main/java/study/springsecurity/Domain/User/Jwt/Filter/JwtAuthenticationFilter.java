package study.springsecurity.Domain.User.Jwt.Filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import study.springsecurity.Domain.User.Jwt.UserDetail.CustomUserDetail;
import study.springsecurity.Domain.User.Jwt.Util.JwtProvider;
import study.springsecurity.Global.Util.RedisUtil;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //JWT를 사용한 인증 필터이다. 이 필터는 HTTP 요청에 포함된 JWT 토큰을 검증하고,
    // 유효한 경우 사용자 인증 정보를 컨텍스트에 추가한다.

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("[*] Jwt Filter");

        try {
            //HttpServletRequest(Http요청의 정보를 표현하는 객체)에서 AccessToken을 추출한다
            String accessToken = jwtProvider.resolveAccessToken(request);

            // accessToken 없이 접근할 경우
            if (accessToken == null) {
                //다음 필터로 넘어간다.
                // accessToken이 없는데 왜 다음 필터로 넘어가는지?
                // 인증이 필요하지 않은 요청의 경우(로그인, 회원가입 등등)는 여전히 필터 체인에서 계속 처리되어야한다.
                filterChain.doFilter(request, response);
                return;
            }

            // logout 처리된 accessToken
            if (redisUtil.get(accessToken) != null && redisUtil.get(accessToken).equals("logout")) {
                log.info("[*] Logout accessToken");
                // TODO InsufficientAuthenticationException 예외 처리
                log.info("==================");
                filterChain.doFilter(request, response);
                log.info("==================");
                return;
            }

            log.info("[*] Authorization with Token");
            authenticateAccessToken(accessToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("[*] case : accessToken Expired");
        }
    }

    private void authenticateAccessToken(String accessToken) {
        //accessToken을 이용하여 jwtProvider가 사용자 이메일을 추출한다
        //추출된 이메일로 CustomUserDetail 객체를 생성한다
        CustomUserDetail userDetails = new CustomUserDetail(
                jwtProvider.getUserEmail(accessToken),
                null
        );

        log.info("[*] Authority Registration");

        // 스프링 시큐리티 인증 토큰 생성
        //UsernamePasswordAuthentication은 Spring Security에서 사용자 인증 정보를 나타내고
        //인증 과정 및 이후의 권한 검증에 사용되는 중요한 객체이다.
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());


        // 컨텍스트 홀더에 저장을 하게되면, 해당 사용자는 인증된 사용자로 인식되어 애플리케이션의 보호된 리소스에 접근할 수 있게 된다.
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}