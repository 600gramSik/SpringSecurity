package study.springsecurity.Domain.User.Jwt.UserDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.springsecurity.Domain.User.Entity.User;
import study.springsecurity.Domain.User.Exception.UserExceptionHandler;
import study.springsecurity.Domain.User.Repository.UserJpaRepository;
import study.springsecurity.Global.Common.ErrorCode;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByEmail(username).orElseThrow(() -> new UserExceptionHandler(ErrorCode.USER_NOT_FOUND));
        log.info("[*] User found : " + user.getEmail());
        return new CustomUserDetail(user);
    }
}
