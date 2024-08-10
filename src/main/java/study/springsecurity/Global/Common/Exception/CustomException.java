package study.springsecurity.Global.Common.Exception;

import lombok.Getter;
import study.springsecurity.Global.Common.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
