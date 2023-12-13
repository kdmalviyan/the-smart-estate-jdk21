package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.bookings.Booking;
import com.sfd.thesmartestate.bookings.BookingResponseDto;
import com.sfd.thesmartestate.lms.followups.Followup;

import java.util.Set;

public class BookingResponseMapper {

    private BookingResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialized");
    }

    public static BookingResponseDto mapToBookingResponse(Booking booking, Set<Followup> leadFollowups) {

        return BookingResponseDto.builder()
                .id(booking.getId())
                .buyer(booking.getBuyer())
                .coBuyer(booking.getCoBuyer())
                .project(ProjectResponseMapper.mapToProjectResponse(booking.getProject()))
                .inventory(booking.getInventory())
                .sellingPrice(booking.getSellingPrice())
                .discount(booking.getDiscount())
                .discountPercent(booking.getDiscountPercent())
                .businessManager(UserResponseMapper.mapToUserResponse(booking.getBusinessManager()))
                .businessExecutive(UserResponseMapper.mapToUserResponse(booking.getBusinessExecutive()))
                .businessHead(UserResponseMapper.mapToUserResponse(booking.getBusinessHead()))
                .channelPartner(booking.getChannelPartner())
                .applicationFormPath(booking.getApplicationFormPath())
                .buyerPanCardPath(booking.getBuyerPanCardPath())
                .buyerAadhaarCardPath(booking.getBuyerAadhaarCardPath())
                .coBuyerAadhaarCardPath(booking.getCoBuyerAadhaarCardPath())
                .coBuyerPanCardPath(booking.getCoBuyerPanCardPath())
                .paymentCopyPath(booking.getPaymentCopyPath())
                .remark(booking.getRemark())
                .createdBy(UserResponseMapper.mapToUserResponse(booking.getCreatedBy()))
                .updatedBy(UserResponseMapper.mapToUserResponse(booking.getUpdatedBy()))
                .createdAt(booking.getCreatedAt())
                .lastUpdatedAt(booking.getLastUpdatedAt())
                .bookingDate(booking.getBookingDate())
                .isActive(booking.getIsActive())
                .lead(LeadResponseMapper.mapToLeadResponse(booking.getLead(), leadFollowups)).build();
    }
}
