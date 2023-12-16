package com.sfd.thesmartestate.security.services;

import com.sfd.thesmartestate.security.JwtTokenGenerator;
import com.sfd.thesmartestate.security.entities.RefreshToken;
import com.sfd.thesmartestate.security.exceptions.TokenRefreshException;
import com.sfd.thesmartestate.security.repository.RefreshTokenRepository;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.entities.LoginDetails;
import com.sfd.thesmartestate.users.repositories.UserRepository;
import com.sfd.thesmartestate.users.services.LoginDetailsService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
    private final LoginDetailsService loginDetailsService;

   public RefreshTokenService(final RefreshTokenRepository refreshTokenRepository,
                              final JwtTokenGenerator jwtTokenGenerator,
                              final UserRepository userRepository,
                              final LoginDetailsService loginDetailsService) {
       this.jwtTokenGenerator = jwtTokenGenerator;
       this.refreshTokenRepository = refreshTokenRepository;
       this.userRepository = userRepository;
       this.loginDetailsService = loginDetailsService;
   }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        Employee employee = userRepository.findByUsername(username);
        try {
            RefreshToken refreshToken = new RefreshToken();
            RefreshToken oldRefreshToken = refreshTokenRepository.findByEmployee(employee);
            if(Objects.nonNull(oldRefreshToken)) {
                refreshToken = oldRefreshToken;
            }
            LoginDetails loginDetails = loginDetailsService.findByUsername(username);
            refreshToken.setToken(jwtTokenGenerator.generate(loginDetails, true));
            refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(tokenExpiryInMinutes));
            refreshToken.setEmployee(employee);
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