package study.springsecurity.Domain.User.Jwt.Exception;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import study.springsecurity.Domain.User.Jwt.Util.HttpResponseUtil;
import study.springsecurity.Global.Common.ApiResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //AuthenticationEntryPoint는 Spring Security에서 인증되지 않은 사용자가 보호된 자원에 접근할 때 예외 처리를 담당하는 인터페이스이다.
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        HttpStatus httpStatus;
        ApiResponse<String> errorResponse;

        log.error(">>>>>> AuthenticationException: ", authException);
        httpStatus = HttpStatus.UNAUTHORIZED;
        errorResponse = ApiResponse.onFailure(
                TokenErrorCode.UNAUTHORIZED.getCode(),
                TokenErrorCode.UNAUTHORIZED.getMessage(),
                authException.getMessage());
        HttpResponseUtil.setErrorResponse(response, httpStatus, errorResponse);
    }
}
