package io.github.manishdait.aquila.auth.token.referesh;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefereshTokenService {
    private final RefereshTokenRepository refereshTokenRepository;

    public String generateToken() {
        String token = UUID.randomUUID().toString();
        RefereshToken refereshToken = RefereshToken.builder().token(token).createdAt(Instant.now()).build();
        refereshTokenRepository.save(refereshToken);

        return token;
    }

    public boolean isValidToken(String token) {
        Optional<RefereshToken> refreshToken = refereshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            return true;
        } 
        return false;
    }

    public void deleteToken (String token) {
        refereshTokenRepository.deleteByToken(token);
    }
}
