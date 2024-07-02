package io.github.manishdait.aquila.auth.token.referesh;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereshTokenRepository extends JpaRepository <RefereshToken, Long> {
    Optional<RefereshToken> findByToken(String token);
    void deleteByToken(String token);
}
