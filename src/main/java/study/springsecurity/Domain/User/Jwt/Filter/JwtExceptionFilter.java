package study.springsecurity.Domain.User.Jwt.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import study.springsecurity.Domain.User.Jwt.Exception.SecurityCustomException;
import study.springsecurity.Domain.User.Jwt.Exception.TokenErrorCode;
import study.springsecurity.Domain.User.Jwt.Util.HttpResponseUtil;
import study.springsecurity.Global.Common.ApiResponse;
import study.springsecurity.Global.Common.BaseErrorCode;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
//HTTP 요청을 처리하는 과정에서 발생할 수 있는 예외를 처리하는 역할을 한다.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException {
        try {
            //필터 체인의 다음 필터로 요청과 응답 객체를 전달한다.
            filterChain.doFilter(request, response);
        } catch (SecurityCustomException e) {
            //securityCustomException 예외가 발생하면, 경고 로그를 기록
            log.warn(">>>>> SecurityCustomException : ", e);
            //예외에서 에러 코드를 가져온다.
            BaseErrorCode errorCode = e.getErrorCode();
            ApiResponse<String> errorResponse = ApiResponse.onFailure(
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    e.getMessage()
            );
            // 응답 객체에 에러 응답을 설정하고 클라이언트에게 반환한다.
            // HTTP 상태 코드도 함께 설정된다.
            HttpResponseUtil.setErrorResponse(
                    response,
                    errorCode.getHttpStatus(),
                    errorResponse
            );
        } catch (Exception e) {
            log.error(">>>>> Exception : ", e);
            // 내부 보안 오류와 관련된 실패 응답 객체를 생성한다.
            ApiResponse<String> errorResponse = ApiResponse.onFailure(
                    TokenErrorCode.INTERNAL_SECURITY_ERROR.getCode(),
                    TokenErrorCode.INTERNAL_SECURITY_ERROR.getMessage(),
                    e.getMessage()
            );
            // 응답 객체에 에러 응답을 설정하고 클라이언트에게 반환한다.
            // 이때 HTTP 상태 코드는 500 INTERNAL SERVER ERROR로 설정된다.
            HttpResponseUtil.setErrorResponse(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    errorResponse
            );
        }
    }
}
