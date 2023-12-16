package com.sfd.thesmartestate.users;

import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import com.sfd.thesmartestate.notifications.enums.OTPTarget;
import com.sfd.thesmartestate.notifications.enums.OTPType;
import com.sfd.thesmartestate.notifications.services.OTPService;
import com.sfd.thesmartestate.notifications.web.EmailNotificationMessage;
import com.sfd.thesmartestate.users.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.users.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.exceptions.UserManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHelper {
    private final OTPService otpService;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])" // Numbers
            + "(?=.*[a-z])" // Lower char
            + "(?=.*[A-Z])" // Upper char
            + "(?=.*[@#$%^&+=])" // Special Chars
            + "(?=\\S+$)" // White spaces
            + ".{8,50}$"; // Min-Max

    public void validateRequestPayload(ChangePasswordRequestPayload requestPayload) {
        log.info("Validating password change request");
        validatePasswords(Objects.isNull(requestPayload.getOldPassword()), "Old password can't be null");
        validatePasswords(Objects.isNull(requestPayload.getPassword()), "Password can't be null");
        validatePasswords(Objects.isNull(requestPayload.getRePassword()), "RePassword can't be null");
        validatePasswords(!Objects.equals(requestPayload.getRePassword(), requestPayload.getPassword()), "Password and RePassword must be same");
        validatePasswords(Objects.equals(requestPayload.getOldPassword(), requestPayload.getPassword()), "Old Password and new Password can't be same");
        validateRegex(requestPayload.getPassword());
        log.info("ChangePasswordRequestPayload validated");
    }

    private void validateRegex(String requestPayload) {
        Pattern p = Pattern.compile(PASSWORD_REGEX);
        Matcher m = p.matcher(requestPayload);
        if (!m.matches()) {
            log.error("Password does not fulfill required criteria, try again");
            throw new UserManagementException("Password does not fulfill required criteria, try again");
        }
    }

    private void validatePasswords(boolean requestPayload, String msg) {
        if (requestPayload) {
            log.error(msg);
            throw new UserManagementException(msg);
        }
    }

    public void validateResetRequest(ResetPasswordRequestPayload requestPayload) {
        log.info("Validating reset password change request");
        validatePasswords(Objects.isNull(requestPayload.getUsername()), "Username can't be null");
        validatePasswords(Objects.isNull(requestPayload.getOtp()), "OTP can't be null");
        validatePasswords(Objects.isNull(requestPayload.getPassword()), "Password can't be null");
        validatePasswords(Objects.isNull(requestPayload.getRePassword()), "RePassword can't be null");
        validateRegex(requestPayload.getPassword());
        log.info("ResetPasswordRequestPayload validated");
    }

    public NotificationMessage createPasswordChangeOTPEmailMessage(Employee employee) {
        log.info("Creating OTP for password change");
        String otp = otpService.generateOTP();
        String message = "Dear User! " +
                "Your OTP for password change is here. " +
                otp +
                " This is valid for 15 min.";
        String subject = "OTP: Password Change";
        boolean isTransactional = true;
        NotificationMessage notificationMessage = new EmailNotificationMessage(message, subject, isTransactional);
        saveOneTimePassword(employee, otp);
        log.info("OTP for password change is created");
        return notificationMessage;
    }

    private void saveOneTimePassword(Employee employee, String otpValue) {
        OneTimePassword otp = new OneTimePassword();
        otp.setValue(otpValue);
        otp.setTarget(OTPTarget.EMAIL);
        otp.setType(OTPType.FORGOT_PASSWORD);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setUsed(false);
        otp.setUsername(employee.getUsername());
        otp.setCreatedBy(employee);
        OneTimePassword oldOTP = otpService.findByUsername(employee.getUsername());
        if (Objects.nonNull(oldOTP)) {
            otp.setId(oldOTP.getId());
        }
        otpService.save(otp);
    }

    public OneTimePassword validateOTP(Employee employee, String oneTimePassword) {
        log.info("Validating OPT for password change for " + employee.getUsername());
        OneTimePassword otp = otpService.findByUsername(employee.getUsername());
        otpService.checkOtpUsed(otp);
        otpService.checkOtpValue(oneTimePassword, otp);
        otpService.checkOtpExpired(otp);
        log.info("OTP validated");
        return otp;
    }

}
