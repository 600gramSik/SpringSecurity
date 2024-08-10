package study.springsecurity.Domain.User.Jwt.Exception;

import lombok.Getter;
import study.springsecurity.Global.Common.BaseErrorCode;
import study.springsecurity.Global.Common.Exception.CustomException;

@Getter
public class SecurityCustomException extends CustomException {
    private final Throwable cause;

    public SecurityCustomException(BaseErrorCode errorCode) {
        super(errorCode);
        this.cause = null;
    }

    public SecurityCustomException(BaseErrorCode errorCode, Throwable cause) {
        super(errorCode);
        this.cause = cause;
    }
}