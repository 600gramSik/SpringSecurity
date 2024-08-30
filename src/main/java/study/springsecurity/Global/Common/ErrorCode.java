package study.springsecurity.Global.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {
    //특정 도메인이나 모듈에서 발생하는 구체적이고 특화된 오류를 다룬다.
    //USER
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER401", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER402", "사용자가 이미 존재합니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER403", "비밀번호가 일치하지않습니다."),
    NO_AUTHORIZATION(HttpStatus.BAD_REQUEST, "USER404", "권한이 없습니다."),
    //POST
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST401", "게시물이 존재하지않습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
