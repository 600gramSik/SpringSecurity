package study.springsecurity.Domain.User.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "account_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    /**
     * JSON 데이터를 객체로 역직렬화(즉, 클라이언트가 서버로 데이터를 보낼 때)할 때는 password 필드에 값을 할당할 수 있다.
     * 반면에, 객체를 JSON으로 직렬화(즉, 서버가 클라이언트에게 데이터를 응답할 때)할 때는 password 필드가 JSON 출력에 포함되지 않는다.
     * 이 설정은 보안을 강화하기 위해 사용된다. 예를 들어, 사용자의 비밀번호를 포함한 객체가 클라이언트에게 반환될 때 비밀번호가 노출되다 않도록 하기 위함이.
     */
    @Column(name = "password")
    private String password;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickName;
}
