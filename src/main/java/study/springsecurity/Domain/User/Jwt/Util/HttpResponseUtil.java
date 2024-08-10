package study.springsecurity.Domain.User.Jwt.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import study.springsecurity.Global.Common.ApiResponse;

import java.io.IOException;
@Slf4j
public class HttpResponseUtil {
    //Springboot에서 HTTP 응답을 생성하고 전송하는 작업을 단순화하기 위해 사용이 된다.
    private static final ObjectMapper objectMapper = new ObjectMapper();
    //objectMapper는 Java객체와 Json간의 직렬화/역직렬화를 도와준다.

    public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws IOException {
        //IOException은 파일 읽기/쓰기, 네트워크 통신, 스트림 작업 등 다양한 I/O 작업을 수행할 때 발생할 수 있는 오류를 처리하기 위해 사용된다.
        log.info("[*] Success Response");
        String responseBody = objectMapper.writeValueAsString(ApiResponse.onSuccess(body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //응답형태는 JSON이라는 것을 명시
        response.setStatus(httpStatus.value());
        //요청이 성공적인지, 오류가 있는지 나타낸다
        response.setCharacterEncoding("UTF-8");
        //문자 인코딩
        response.getWriter().write(responseBody);
        //JSON 형식의 응답 데이터를 클라이언트에게 전송
    }

    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
            throws IOException {
        log.info("[*] Failure Response");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}