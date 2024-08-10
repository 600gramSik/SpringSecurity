package study.springsecurity.Domain.User.Exception;

import study.springsecurity.Global.Common.BaseErrorCode;
import study.springsecurity.Global.Common.Exception.CustomException;

public class UserExceptionHandler extends CustomException {
    public UserExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}