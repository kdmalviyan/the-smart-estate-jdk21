package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import com.sfd.thesmartestate.notifications.enums.OTPTarget;
import com.sfd.thesmartestate.notifications.enums.OTPType;
import com.sfd.thesmartestate.employee.entities.Employee;

public interface OTPService {
    OneTimePassword save(OneTimePassword otp);

    OneTimePassword findByUsername(String username);

    OneTimePassword saveOneTimePassword(Employee employee, String otpValue, OTPTarget target, OTPType otpType);

    void checkOtpUsed(OneTimePassword otp);

    void markOTPUsed(OneTimePassword oneTimePassword);

    void checkOtpExpired(OneTimePassword otp);

    void checkOtpValue(String oneTimePassword, OneTimePassword otp);

    String generateOTP();
}
