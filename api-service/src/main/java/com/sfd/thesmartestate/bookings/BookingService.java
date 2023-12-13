package com.sfd.thesmartestate.bookings;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookingService {
    Booking create(
            String booking,
            MultipartFile applicationForm,
            MultipartFile buyerPan,
            MultipartFile buyerAadhaar,
            MultipartFile paymentCopy,
            MultipartFile coBuyerAadhaar,
            MultipartFile coBuyerPan,
            String vendorId
    ) throws JsonProcessingException;

    Booking update(Booking booking);

    Booking findById(Long id);

    List<BookingResponseDto> findAll();

    void delete(Booking booking);
}
