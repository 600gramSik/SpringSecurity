package study.springsecurity.Domain.User.Service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.springsecurity.Domain.User.Dto.Request.UserLoginRequestDto;
import study.springsecurity.Domain.User.Dto.Request.UserSignupRequestDto;
import study.springsecurity.Domain.User.Dto.Response.UserLoginResponseDto;
import study.springsecurity.Domain.User.Dto.Response.UserSignupResponseDto;
import study.springsecurity.Domain.User.Entity.User;
import study.springsecurity.Domain.User.Exception.UserExceptionHandler;
import study.springsecurity.Domain.User.Jwt.Exception.SecurityCustomException;
import study.springsecurity.Domain.User.Jwt.Exception.TokenErrorCode;
import study.springsecurity.Domain.User.Jwt.UserDetail.CustomUserDetail;
import study.springsecurity.Domain.User.Jwt.Util.JwtProvider;
import study.springsecurity.Domain.User.Repository.UserJpaRepository;
import study.springsecurity.Global.Common.ErrorCode;
import study.springsecurity.Global.Util.RedisUtil;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        // 회원 정보 존재 하는지 확인
        User user = userJpaRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.USER_NOT_FOUND));

        // 회원 pw 일치 여부
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserExceptionHandler(ErrorCode.PASSWORD_NOT_MATCH);
        }

        CustomUserDetail customUserDetails = new CustomUserDetail(user);

        // 로그인 성공 시 토큰 생성
        String accessToken = jwtProvider.createJwtAccessToken(customUserDetails);
        String refreshToken = jwtProvider.createJwtRefreshToken(customUserDetails);

        return UserLoginResponseDto.from(user, accessToken, refreshToken);
    }

    public UserSignupResponseDto signup(UserSignupRequestDto request) {
        // pw, pw 확인 일치 확인
        if (!request.password().equals(request.passwordCheck()))
            throw new UserExceptionHandler(ErrorCode.PASSWORD_NOT_MATCH);

        //이메일 중복 확인
        if (userJpaRepository.existsByEmail(request.email())) {
            throw new UserExceptionHandler(ErrorCode.USER_ALREADY_EXIST);
        }

        //사용자가 입력한 비밀번호를 암호화해준다.
        String encodedPw = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPw);

        return UserSignupResponseDto.from(userJpaRepository.save(user));
    }

    public void logout(HttpServletRequest request) {
        try {
            String accessToken = jwtProvider.resolveAccessToken(request);
            if (accessToken != null) {
                String userEmail = jwtProvider.getUserEmail(accessToken);
                String redisKey = userEmail + ":refresh";
                String refreshToken = (String) redisUtil.get(redisKey);

                // accessToken으루 이용하여 추출한 refreshToken을 블랙리스트에 추가
                if (refreshToken != null) {
                    redisUtil.save(
                            "blacklist:" + refreshToken,
                            userEmail,
                            jwtProvider.getExpTime(refreshToken),
                            TimeUnit.MILLISECONDS
                    );
                    redisUtil.delete(redisKey);
                }
            }
        } catch (ExpiredJwtException e) {
            throw new SecurityCustomException(TokenErrorCode.TOKEN_EXPIRED);
        }
    }


}
