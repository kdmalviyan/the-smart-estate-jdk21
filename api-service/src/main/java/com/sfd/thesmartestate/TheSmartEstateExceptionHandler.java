package com.sfd.thesmartestate;

import com.sfd.thesmartestate.bookings.BookingException;
import com.sfd.thesmartestate.bookings.BookingResponsePayload;
import com.sfd.thesmartestate.common.dto.ExceptionDto;
import com.sfd.thesmartestate.common.exceptions.FileException;
import com.sfd.thesmartestate.employees.vacation.exceptions.VacationException;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.exceptions.TargetException;
import com.sfd.thesmartestate.notifications.exceptions.OneTimePasswordException;
import com.sfd.thesmartestate.security.exceptions.*;
import com.sfd.thesmartestate.thirdparty.exceptions.RemoteServiceNotAvailableException;
import com.sfd.thesmartestate.thirdparty.exceptions.ThirdPartyExceptions;
import com.sfd.thesmartestate.users.exceptions.UserManagementException;
import com.sfd.thesmartestate.users.teams.exceptions.TeamException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

/**
 * @author kuldeep
 */
@ControllerAdvice
@Slf4j
public class TheSmartEstateExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BookingException.class)
    public ResponseEntity<BookingResponsePayload> handleException(BookingException exception) {
        return ResponseEntity.badRequest().body(
                BookingResponsePayload
                        .builder()
                        .withStatus(HttpStatus.BAD_REQUEST)
                        .withMessage("Something went wrong, please check with admin. " + exception.getMessage())
                        .build());
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ExceptionDto> handleException(FileException exception) {
        return ResponseEntity.badRequest().body(ExceptionDto.builder().message(exception.getMessage()).build());
    }


    public ResponseEntity<ExceptionDto> handleException(QueryException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(ExceptionDto.builder().message("Error in executing process").build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionDto> handleException(IllegalStateException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(ExceptionDto.builder().message("Something went wrong").build());
    }

    @ExceptionHandler(VacationException.class)
    public ResponseEntity<?> handleException(VacationException vacationException) {
        return ResponseEntity.badRequest().body(vacationException.getMessage());
    }

    @ExceptionHandler(LeadException.class)
    public ResponseEntity<ExceptionDto> handleException(LeadException exception) {
        return ResponseEntity.badRequest().body(ExceptionDto.builder().message(exception.getMessage()).status(HttpStatus.EXPECTATION_FAILED.value()).build());
    }

    @ExceptionHandler(TargetException.class)
    public ResponseEntity<ExceptionDto> handleException(TargetException exception) {
        return ResponseEntity.badRequest().body(ExceptionDto.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(OneTimePasswordException.class)
    public ResponseEntity<ExceptionDto> handleException(OneTimePasswordException exception) {
        return ResponseEntity.ok(ExceptionDto.builder().message(exception.getMessage()).status(500).build());
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<?> handleException(InvalidCredentialsException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build());
    }

    @ExceptionHandler(JWTTokenGenerationException.class)
    public ResponseEntity<?> handleException(JWTTokenGenerationException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build());
    }

    @ExceptionHandler(PasswordMismatchedException.class)
    public ResponseEntity<?> handleException(PasswordMismatchedException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build());
    }

    @ExceptionHandler(value = TenantUpdateException.class)
    public ResponseEntity<?> handleException(TenantUpdateException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.FORBIDDEN.value()).build());
    }

    @ExceptionHandler(value = UnauthorizedOperationException.class)
    public ResponseEntity<?> handleException(UnauthorizedOperationException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.FORBIDDEN.value()).build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.NOT_FOUND.value()).build());
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<?> handleException(TokenRefreshException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleException(UsernameNotFoundException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.NOT_FOUND.value()).build());
    }

    @ExceptionHandler(UserDisableException.class)
    public ResponseEntity<?> handleException(UserDisableException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.LOCKED.value()).build());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> handleException(BadCredentialsException th) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ExceptionDto.builder().message(th.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build());
    }

    @ExceptionHandler(RemoteServiceNotAvailableException.class)
    public void handleException(RemoteServiceNotAvailableException exceptions) {
        log.error("Remote lead fetch failed: " + exceptions.getMessage());
    }

    @ExceptionHandler(ThirdPartyExceptions.class)
    public void handleException(ThirdPartyExceptions exceptions) {
        log.error("Remote lead fetch failed: " + exceptions.getMessage());
    }

    @ExceptionHandler(UserManagementException.class)
    public ResponseEntity<ExceptionDto> handle(UserManagementException exception) {
        return ResponseEntity.ok(ExceptionDto.builder().message(exception.getMessage()).status(500).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception exception) {
        log.error(exception.getMessage());
        if (exception instanceof MaxUploadSizeExceededException) {
            return ResponseEntity.badRequest().body(ExceptionDto.builder().message(Objects.requireNonNull(((MaxUploadSizeExceededException) exception).getRootCause()).getMessage()).build());
        }
        return ResponseEntity.badRequest().body(ExceptionDto.builder().message("Something went wrong").build());
    }

    @ExceptionHandler(TeamException.class)
    public ResponseEntity<ExceptionDto> handleException(TeamException teamException) {
        return ResponseEntity.ok(ExceptionDto.builder().message(teamException.getMessage()).status(500).build());
    }
}
