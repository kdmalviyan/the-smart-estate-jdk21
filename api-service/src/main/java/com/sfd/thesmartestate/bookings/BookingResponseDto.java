package com.sfd.thesmartestate.bookings;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.lms.dto.LeadResponseDto;
import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.inventory.Inventory;
import com.sfd.thesmartestate.users.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class BookingResponseDto {

    private Long id;
    private Customer buyer;
    private String coBuyer;
    private ProjectDTO project;

    // TODO: Replace with Inventory DTO
    private Inventory inventory;
    private Double sellingPrice;
    private Double discount;
    private float discountPercent;
    private UserResponse businessExecutive;
    private UserResponse businessManager;
    private UserResponse businessHead;
    private String channelPartner;
    private String applicationFormPath;
    private String buyerPanCardPath;
    private String coBuyerPanCardPath;
    private String buyerAadhaarCardPath;
    private String coBuyerAadhaarCardPath;
    private String paymentCopyPath;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime bookingDate;
    private Boolean isActive;
    private UserResponse createdBy;
    private LocalDateTime lastUpdatedAt;
    private UserResponse updatedBy;
    private LeadResponseDto lead;
}
