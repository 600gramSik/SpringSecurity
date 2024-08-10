package study.springsecurity.Global.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.springsecurity.Domain.User.Jwt.Exception.JwtAccessDeniedHandler;
import study.springsecurity.Domain.User.Jwt.Exception.JwtAuthenticationEntryPoint;
import study.springsecurity.Domain.User.Jwt.Filter.JwtAuthenticationFilter;
import study.springsecurity.Domain.User.Jwt.Filter.JwtExceptionFilter;
import study.springsecurity.Domain.User.Jwt.Util.JwtProvider;
import study.springsecurity.Global.Util.RedisUtil;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //인증이 필요 없는 URL 배열 정의
    private final String[] authUrls = {"/", "/user/signup/**",  "/user/login/**",
             "/user/reissue/**"};


    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    //AuthenticationManager는 스프링 시큐리티에서 사용자의 인증을 처리하는 인터페이스이다.
    //주어진 인증 요청을 받아서 인증을 시도하고, 성공하면 객체를 반환, 실패하면 예외를 던진다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cors를 활성화 해준다.
        //cors를 활성화하면 백엔드 서버는 도메인에서 오는 요청을 허용하도록 설정할 수 있다.
        //이를 통해 프론트엔드는 안전하게 API 서버에 요청을 보낼 수 있다.
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.apiConfigurationSource()));

        // csrf disable
        //spring security는 csrf 공격 방지를 위해 csrf 보호 기능을 활성화한다.
        /*
        하지만 이를 왜 비활성화하나?
        RESTful API는 상태를 유지하지 않고, 클라이언트와 서버간의 통신은 JWT와 같은 토큰을 통해
        이루어져서 CSRF 보호가 필요하지 않다.
         */
        http
                .csrf(AbstractHttpConfigurer::disable);

        // form 로그인 방식 disable
                /*
        Spring Security는 보통 form login을 제공한다.
        form login은 사용자 이름과 비밀번호를 입력받아 인증하는 방식이다.
        이것도 왜 비활성화를 하냐?
        우리는 JWT을 사용한 인증 방식을 사용하고 있어서 form login 기능이 필요없다. 따라서 이 기능을 비활성화해서
        불필요한 로그인 페이지 제공을 방지한다.
         */

        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        /*
        이것도 JWT를 사용하므로 비활성화한다.
         */
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //JWT인증 필터를 추가한다.
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, redisUtil), UsernamePasswordAuthenticationFilter.class);
        // JWT 예외처리 필터
        http
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        // 경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> auth
                        //위에서 설정한 authUrls을 모든 사용자에게 접근을 허용하게 한다.
                        .requestMatchers(authUrls).permitAll()
                        //나머지 모든 경로에 대해 인증된 사용자만 접근할 수 있도록 설정한다.
                        .requestMatchers("/**").authenticated()
                        .anyRequest().permitAll()
                );

        // 예외 처리
        http
                .exceptionHandling(exception -> exception
                        //인증되지 않은 사용자가 보호된 리소스에 접근하려할 때 발생하는 예외
                        //401 Unauthorized 상태 코드와 함께 에러 메세지를 클라이언트에게 반환하는 역할을 한다.
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        //인증된 사용자가 권한이 부족해(사용자가 인증은 되었으나, 필요한 권한이 없는 경우) 접근할 수 있는 리소스에 접근하려 할 때
                        //403 Forbidden 상태 코드와 함께 에러메세지를 반환한다.
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                );

        // 세션 사용 안함
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}