package goormcoder.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "t_refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", nullable = false)
    private String token;

    @Builder
    private RefreshToken(String token) {
        this.token = token;
    }

    public static RefreshToken of(String token) {
        return RefreshToken.builder()
                .token(token)
                .build();
    }

}
