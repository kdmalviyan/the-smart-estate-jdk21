package com.sfd.thesmartestate.security.services;

import com.sfd.thesmartestate.security.JwtTokenGenerator;
import com.sfd.thesmartestate.security.entities.RefreshToken;
import com.sfd.thesmartestate.security.exceptions.TokenRefreshException;
import com.sfd.thesmartestate.security.repository.RefreshTokenRepository;
import com.sfd.thesmartestate.users.entities.User;
import com.sfd.thesmartestate.users.repositories.UserRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")
public class RefreshTokenService {
    @Value("${security.refresh_token.expires}")
    private Long tokenExpiryInMinutes;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

   public RefreshTokenService(final RefreshTokenRepository refreshTokenRepository,
                              final JwtTokenGenerator jwtTokenGenerator,
                              final UserRepository userRepository) {
       this.jwtTokenGenerator = jwtTokenGenerator;
       this.refreshTokenRepository = refreshTokenRepository;
       this.userRepository = userRepository;
   }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username);
        try {
            RefreshToken refreshToken = new RefreshToken();
            RefreshToken oldRefreshToken = refreshTokenRepository.findByUser(user);
            if(Objects.nonNull(oldRefreshToken)) {
                refreshToken = oldRefreshToken;
            }
            refreshToken.setToken(jwtTokenGenerator.generate(user, true));
            refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(tokenExpiryInMinutes));
            refreshToken.setUser(user);
            return refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            throw new TokenRefreshException(e.getMessage());
        }
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
        }
    }
}