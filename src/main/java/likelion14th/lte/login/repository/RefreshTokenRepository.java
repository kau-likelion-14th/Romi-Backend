package likelion14th.lte.login.repository;

import likelion14th.lte.login.entity.RefreshTokenEntity;
import likelion14th.lte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUser(User user);
}
