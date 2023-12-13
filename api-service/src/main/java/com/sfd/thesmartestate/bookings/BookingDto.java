package com.sfd.thesmartestate.bookings;

import com.sfd.thesmartestate.customer.entities.Customer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
public class BookingDto {

    private Long id;
    private Long leadId;
    private Customer buyer;
    private String coBuyerName;
    private Long projectId;
    private Long inventoryId;
    private Double sellingPrice;
    private Long businessExecutiveId;
    private Long businessManagerId;
    private Long businessHeadId;
    private String channelPartner;
    private String remark;
}
