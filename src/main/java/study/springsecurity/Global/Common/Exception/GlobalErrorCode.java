package study.springsecurity.Global.Common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import study.springsecurity.Global.Common.ApiResponse;
import study.springsecurity.Global.Common.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    //애플리케이션 전반에서 공통적으로 발생할 수 있는 일반적인 오류를 정의한다. 이 오류들은 특정 도메인에 국한되지 않으며, 애플리케이션의 다양한 부분에서 재사용될 수 있디.
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "ERROR4000", "입력값에 대한 검증에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5000", "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }

}