package study.springsecurity.Domain.User.Jwt.Util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class HttpResponseUtil {
    public static void setSuccessResponse(HttpServletResponse response, HttpStatus status,
                                          Object body) throws IOException {
        //IOException은 파일 읽기/쓰기, 네트워크 통신, 스트림 작업 등 다양한 I/O 작업을 수행할 때 발생할 수 있는 오류를 처리하기 위해 사용된다.

    }
}