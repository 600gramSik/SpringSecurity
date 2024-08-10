package study.springsecurity.Global.Config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorsConfig implements WebMvcConfigurer {
    public static CorsConfigurationSource apiConfigurationSource() {
        // CORS 설정 객체를 생성
        CorsConfiguration configuration = new CorsConfiguration();

        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:8080");

        ArrayList<String> allowedHttpMethods = new ArrayList<>();
        allowedHttpMethods.add("GET");
        allowedHttpMethods.add("POST");

        configuration.setAllowedOrigins(allowedOriginPatterns);
        configuration.setAllowedMethods(allowedHttpMethods);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //서버에 들어오는 모든 경로("/**")에 대해, 지정된 CORS 설정(configuration)을 적용한다는 것을 의미한다.
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
