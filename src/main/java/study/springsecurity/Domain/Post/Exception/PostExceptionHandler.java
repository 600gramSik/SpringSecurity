package study.springsecurity.Domain.Post.Exception;

import study.springsecurity.Global.Common.BaseErrorCode;
import study.springsecurity.Global.Common.Exception.CustomException;

public class PostExceptionHandler extends CustomException {
    public PostExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}

