package study.springsecurity.Domain.User.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springsecurity.Domain.User.Dto.Request.UserLoginRequestDto;
import study.springsecurity.Domain.User.Dto.Request.UserSignupRequestDto;
import study.springsecurity.Domain.User.Dto.Response.UserLoginResponseDto;
import study.springsecurity.Domain.User.Dto.Response.UserSignupResponseDto;
import study.springsecurity.Domain.User.Service.UserService;
import study.springsecurity.Global.Common.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto request) {
        return ApiResponse.onSuccess(userService.login(request));
    }

    @PostMapping("/signup")
    public ApiResponse<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto request) {
        return ApiResponse.onSuccess(userService.signup(request));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.onSuccess("로그아웃 성공");
    }

}
