package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.OTPGenerator;
import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import com.sfd.thesmartestate.notifications.enums.OTPTarget;
import com.sfd.thesmartestate.notifications.enums.OTPType;
import com.sfd.thesmartestate.notifications.exceptions.OneTimePasswordException;
import com.sfd.thesmartestate.notifications.repositories.OTPRepository;
import com.sfd.thesmartestate.users.entities.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OTPRepository otpRepository;
    private final OTPGenerator otpGenerator;

    @Override
    public OneTimePassword save(OneTimePassword otp) {
        return otpRepository.save(otp);
    }

    @Override
    public OneTimePassword findByUsername(String username) {
        return otpRepository.findByUsername(username);
    }


    public OneTimePassword saveOneTimePassword(Employee employee, String otpValue, OTPTarget target, OTPType otpType) {
        OneTimePassword otp = new OneTimePassword();
        otp.setValue(otpValue);
        otp.setTarget(target);
        otp.setType(otpType);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setUsed(false);
        otp.setUsername(employee.getUsername());
        otp.setCreatedBy(employee);
        OneTimePassword oldOTP = findByUsername(employee.getUsername());
        if (Objects.nonNull(oldOTP)) {
            otp.setId(oldOTP.getId());
        }
        return save(otp);
    }

    public void checkOtpUsed(OneTimePassword otp) {
        if (otp.isUsed()) {
            throw new RuntimeException(new OneTimePasswordException("OTP is already used, please get a new one"));
        }
    }

    public void checkOtpValue(String oneTimePassword, OneTimePassword otp) {
        if (!otp.getValue().equalsIgnoreCase(oneTimePassword)) {
            throw new OneTimePasswordException("OTP is invalid");
        }
    }

    @Override
    public String generateOTP() {
        return otpGenerator.generateOTP();
    }

    public void checkOtpExpired(OneTimePassword otp) {
        LocalDateTime expiryTime = otp.getCreatedAt().plus(15, ChronoUnit.MINUTES);
        if (expiryTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException(new OneTimePasswordException("OTP is expired, please get a new one"));
        }
    }

    public void markOTPUsed(OneTimePassword oneTimePassword) {
        oneTimePassword.setUsed(true);
        save(oneTimePassword);
    }


}
