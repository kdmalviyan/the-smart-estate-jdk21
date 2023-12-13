package com.sfd.thesmartestate.notifications;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Slf4j
@Component
public class OTPGenerator {
    public String generateOTP() {
        TOTP totp = new TOTP.Builder(SecretGenerator.generate())
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA1) // SHA256 and SHA512 are also supported
                .withPeriod(Duration.ofSeconds(30)).build();
        return totp.now();
    }
}
