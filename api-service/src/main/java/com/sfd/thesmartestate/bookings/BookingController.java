package com.sfd.thesmartestate.bookings;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "booking")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BookingController {
    private final BookingService bookingService;


    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookingResponsePayload> create(
            @RequestParam("bookingDetails") String bookingDto,
            @RequestParam("applicationForm") MultipartFile applicationForm,
            @RequestParam("buyerPan") MultipartFile buyerPan,
            @RequestParam("buyerAadhar") MultipartFile buyerAadhar,
            @RequestParam("paymentCopy") MultipartFile paymentCopy,
            @RequestParam(value = "coBuyerPan", required = false) MultipartFile coBuyerPan,
            @RequestParam(value = "coBuyerAadhar", required = false) MultipartFile coBuyerAadhar,
            @RequestHeader("X-VendorID") String vendorId
    ) throws JsonProcessingException {
        return ResponseEntity.status(200).body(BookingResponsePayload
                .builder()
                .withMessage("Created successfully")
                .withStatus(HttpStatus.CREATED)
                .withContent(bookingService.create(bookingDto, applicationForm, buyerPan, buyerAadhar, paymentCopy, coBuyerAadhar, coBuyerPan, vendorId))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponsePayload> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(200).body(BookingResponsePayload
                .builder()
                .withMessage("Found booking")
                .withStatus(HttpStatus.FOUND)
                .withContent(bookingService.findById(id))
                .build());
    }

    @GetMapping()
    public ResponseEntity<BookingResponsePayload> listAll() {
        return ResponseEntity.status(200).body(BookingResponsePayload
                .builder()
                .withMessage("Booking list")
                .withStatus(HttpStatus.OK)
                .withContentList(bookingService.findAll())
                .build());
    }

    @PutMapping
    public ResponseEntity<BookingResponsePayload> update(@RequestBody Booking booking) {
        return ResponseEntity.status(200).body(BookingResponsePayload
                .builder()
                .withMessage("Booking updated")
                .withStatus(HttpStatus.ACCEPTED)
                .withContent(bookingService.update(booking))
                .build());
    }
}
