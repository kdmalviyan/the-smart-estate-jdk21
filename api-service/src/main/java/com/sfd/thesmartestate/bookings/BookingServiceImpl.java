package com.sfd.thesmartestate.bookings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.responsemapper.BookingResponseMapper;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.entities.LeadStatus;
import com.sfd.thesmartestate.lms.followups.FollowupService;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.targets.TargetService;
import com.sfd.thesmartestate.projects.inventory.Inventory;
import com.sfd.thesmartestate.projects.inventory.InventoryService;
import com.sfd.thesmartestate.projects.services.InventoryStatusService;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class BookingServiceImpl implements BookingService {

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final LeadService leadService;
    @Autowired
    private final FollowupService followupService;
    @Autowired
    private final LeadStatusService leadStatusService;

    @Autowired
    private final InventoryService inventoryService;
    @Autowired
    private final InventoryStatusService inventoryStatusService;
    @Autowired
    private final TargetService targetService;


    private final BookingRepository repository;
    private final BookingHelper helper;

    @Override
    public Booking create(
            String bookingObj,
            MultipartFile applicationForm,
            MultipartFile buyerPan,
            MultipartFile buyerAadhaar,
            MultipartFile paymentCopy,
            MultipartFile coBuyerAadhaar,
            MultipartFile coBuyerPan,
            String vendorId
    ) throws JsonProcessingException {
        Booking booking = new Booking();
        ObjectMapper mapper = new ObjectMapper();
        BookingDto bookingDto = mapper.readValue(bookingObj, BookingDto.class);
        helper.validateBeforeCreate(bookingDto);

        //Changing Lead Status
        Lead lead = leadService.findById(bookingDto.getLeadId());
        LeadStatus leadStatus = leadStatusService.findByName(Constants.BOOKED);
        lead.setStatus(leadStatus);
        booking.setLead(lead);

        booking.setBuyer(bookingDto.getBuyer());
        booking.setCoBuyer(bookingDto.getCoBuyerName());
        booking.setBookingDate(LocalDateTime.now());
        booking.setProject(projectService.findById(bookingDto.getProjectId()));
        Inventory inventoryToBook = inventoryService.findById(bookingDto.getInventoryId());
        inventoryToBook.setUpdatedBy(employeeService.findLoggedInEmployee());
        booking.setInventory(inventoryToBook);
        booking.setSellingPrice(bookingDto.getSellingPrice());
        booking.setBusinessExecutive(employeeService.findById(bookingDto.getBusinessExecutiveId()));
        booking.setBusinessManager(employeeService.findById(bookingDto.getBusinessManagerId()));
        booking.setBusinessHead(employeeService.findById(bookingDto.getBusinessHeadId()));
        booking.setChannelPartner(bookingDto.getChannelPartner());
        booking.setRemark(bookingDto.getRemark());

        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        booking.setCreatedAt(LocalDateTime.now());
        booking.setCreatedBy(loggedInEmployee);
        booking.setLastUpdatedAt(LocalDateTime.now());
        booking.setUpdatedBy(loggedInEmployee);
        booking.setIsActive(true);

        // save booking before uploading files so that we can utilize booking id
        Booking persistedBooking = repository.save(booking);
        log.info("Booking created");

        //update inventory status
        inventoryToBook.setInventoryStatus(inventoryStatusService.findByName("BOOKED"));
        inventoryToBook.setLastUpdateAt(LocalDateTime.now());
        inventoryService.save(inventoryToBook);

        // Storing Application Form
        long bookingId = booking.getId();
        String applicationPath = helper.saveToS3AndReturnPath(applicationForm, true, Constants.APPLICATION_FORM, bookingId);
        persistedBooking.setApplicationFormPath(applicationPath);

        // Storing Buyer Pan Card
        String buyerPanPath = helper.saveToS3AndReturnPath(buyerPan, true, Constants.BUYER_PAN_CARD, bookingId);
        persistedBooking.setBuyerPanCardPath(buyerPanPath);

        // Storing Buyer Aadhaar Card
        String buyerAadhaarPath = helper.saveToS3AndReturnPath(buyerAadhaar, true, Constants.BUYER_AADHAAR_CARD, bookingId);
        persistedBooking.setBuyerAadhaarCardPath(buyerAadhaarPath);

        // Storing Payment Cope
        String paymentCopyPath = helper.saveToS3AndReturnPath(paymentCopy, true, Constants.PAYMENT_COPY, bookingId);
        persistedBooking.setPaymentCopyPath(paymentCopyPath);

        // Storing Co-Buyer Aadhaar -- Optional
        if (Objects.nonNull(coBuyerAadhaar)) {
            String coBuyerAadhaarPath = helper.saveToS3AndReturnPath(coBuyerAadhaar, false, Constants.CO_BUYER_AADHAAR_CARD, bookingId);
            persistedBooking.setCoBuyerAadhaarCardPath(coBuyerAadhaarPath);
        }

        // Storing Co-Buyer Pan -- Optional
        if (Objects.nonNull(coBuyerPan)) {
            String coBuyerPanPath = helper.saveToS3AndReturnPath(coBuyerPan, false, Constants.CO_BUYER_PAN_CARD, bookingId);
            persistedBooking.setCoBuyerPanCardPath(coBuyerPanPath);
        }
        Booking bookingUpdated = repository.saveAndFlush(persistedBooking);
        log.info("Booking created with files url");
        targetService.findAndUpdateUserTarget(loggedInEmployee, LeadEvents.STATUS_CHANGED);
        log.info("Target updated successfully");

        return bookingUpdated;
    }

    @Override
    public Booking update(Booking booking) {
        helper.validateBeforeUpdate(booking);
        return repository.save(booking);
    }

    @Override
    public Booking findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BookingException("No booking found with given booking id."));
    }

    @Override
    public List<BookingResponseDto> findAll() {
        Employee employee = employeeService.findLoggedInEmployee();

        if (employee.isAdmin() || employee.isSuperAdmin()) {

            return repository.findAll().stream()
                    .map(booking -> BookingResponseMapper.mapToBookingResponse(booking, followupService.findFollowupByLead(booking.getLead()))).collect(Collectors.toList());
        }
        return repository.findByCreatedBy(employee).stream()
                .map(booking -> BookingResponseMapper.mapToBookingResponse(booking, followupService.findFollowupByLead(booking.getLead()))).collect(Collectors.toList());
    }

    @Override
    public void delete(Booking booking) {
        booking.setIsActive(false);
        repository.save(booking);
    }
}
