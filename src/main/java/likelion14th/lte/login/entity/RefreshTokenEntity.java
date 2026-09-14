package likelion14th.lte.login.entity;


import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.persistence.*;
import likelion14th.lte.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 1000)
    private String refreshTokenEntity;

    @Column(nullable = false)
    private Long refreshTokenExpiration;

    @Builder
    public RefreshTokenEntity(User user, String refreshToken, Long refreshTokenExpiration) {
        this.user = user;
        this.refreshTokenEntity = refreshToken;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public void updateToken(String refreshToken, Long refreshTokenExpiration) {
        this.refreshTokenEntity = refreshToken;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

}
