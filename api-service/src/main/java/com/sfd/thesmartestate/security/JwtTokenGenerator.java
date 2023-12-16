package com.sfd.thesmartestate.security;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.security.certificates.SecurityCertificatesManager;
import com.sfd.thesmartestate.employee.entities.LoginDetails;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Log4j2
@Component
public class JwtTokenGenerator {
    private final SecurityCertificatesManager securityCertificatesManager;

    @Value("${security.token.expires}")
    private int tokenExpiryInMinutes;

    public JwtTokenGenerator(final SecurityCertificatesManager securityCertificatesManager) {
        this.securityCertificatesManager = securityCertificatesManager;
    }

    public String generate(LoginDetails employee, boolean isRefreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Instant now = Instant.now();
        log.info("Generating security tokens");
        return isRefreshToken ? Jwts.builder()
                .claim("isRefreshToken", true)
                .setSubject(employee.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(tokenExpiryInMinutes, ChronoUnit.MINUTES)))
                .signWith(securityCertificatesManager.getPrivateKey())
                .compact()

                : Jwts.builder()
                .claim(Constants.USERNAME, employee.getUsername())
                .claim(Constants.ROLE, employee.getRoles())
                .claim("isRefreshToken", false)
                .setSubject(employee.getUsername())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(tokenExpiryInMinutes, ChronoUnit.MINUTES)))
                .signWith(securityCertificatesManager.getPrivateKey())
                .compact();
    }
}
