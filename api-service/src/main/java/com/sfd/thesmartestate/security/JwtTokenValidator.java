package com.sfd.thesmartestate.security;

import com.sfd.thesmartestate.security.certificates.SecurityCertificatesManager;
import com.sfd.thesmartestate.security.exceptions.InvalidCredentialsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@Component
@Log4j2
public class JwtTokenValidator {
    private final SecurityCertificatesManager securityCertificatesManager;

    public JwtTokenValidator(final SecurityCertificatesManager securityCertificatesManager) {
        this.securityCertificatesManager = securityCertificatesManager;
    }

    public void validate(String jwtToken) {
        try {
            Jws<Claims> jwt = parseJwt(jwtToken);
            if(!StringUtils.hasText(jwt.getBody().getSubject())
                    || Boolean.parseBoolean(Objects.toString(jwt.getBody().get("isRefreshToken")))) {
                throw new InvalidCredentialsException("Provided Token is not valid. Help: Check if token is expired or tempered");
            }
        } catch (Exception e) {
            throw new InvalidCredentialsException("Could not validate Json Token", e);
        }
    }

    public Jws<Claims> parseJwt(String jwtToken) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey publicKey = securityCertificatesManager.getPublicKey();
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwtToken);
    }
}
