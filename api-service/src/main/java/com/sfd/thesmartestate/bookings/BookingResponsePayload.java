package com.sfd.thesmartestate.bookings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Data
@Builder(setterPrefix = "with")
@SuppressFBWarnings("EI_EXPOSE_REP")

public class BookingResponsePayload {
    private String message;
    private HttpStatus status;
    private Booking content;
    private Collection<BookingResponseDto> contentList;
}
