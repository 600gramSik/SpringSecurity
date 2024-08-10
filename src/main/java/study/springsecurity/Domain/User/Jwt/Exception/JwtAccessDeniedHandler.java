package study.springsecurity.Domain.User.Jwt.Exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import study.springsecurity.Domain.User.Jwt.Util.HttpResponseUtil;
import java.io.IOException;
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    //사용자가 인가(Authorization)되지 않은 리소스에 접근하려 할 때 발생하는 예외를 처리
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        //handle메서드는 사용자가 권한없이 접근하려할 때 호출이 된다.
        log.warn("Access Denied: ", accessDeniedException);
        HttpResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN,
                TokenErrorCode.FORBIDDEN.getErrorResponse());
    }
}
