package com.sfd.thesmartestate.bookings;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.services.FileService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class BookingHelper {


    private final FileService fileService;

    @Value("${aws.s3.upload-bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;


    public void validateBeforeCreate(BookingDto booking) {
        //TODO: apply validation logic
    }

    public void validateBeforeUpdate(Booking booking) {
        //TODO: apply validation logic
    }

    public String saveToS3AndReturnPath(MultipartFile file, boolean isRequired, String folderName, long bookingId) {
        if (Objects.nonNull(file)) {
            String path = String.format("%s/%s/%s/%s", bucketName, Constants.BOOKING_FOLDER_NAME, bookingId, folderName);
            String fileName = String.format("%s", file.getOriginalFilename());
            fileService.uploadFileToS3(path, fileName, file);
            return String.format("https://s3." + region + ".amazonaws.com/%s/%s/%s/%s/%s", bucketName, Constants.BOOKING_FOLDER_NAME, bookingId, folderName, fileName);
        } else if (isRequired) {
            throw new BookingException(folderName + "is not present");
        } else {
            return null;
        }
    }
}
