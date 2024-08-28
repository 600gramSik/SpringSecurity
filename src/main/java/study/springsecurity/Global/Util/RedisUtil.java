package study.springsecurity.Global.Util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Component
@RequiredArgsConstructor
public class RedisUtil {
    /*
    RedisConfig 클래스는 Redis 서버와의 연결 설정을 담당하며, RedisTemplate을 통해 애플리케이션에서 Redis와의 상호작용을 쉽게 할 수 있다.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key, Object val, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, val, time, timeUnit);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

}
