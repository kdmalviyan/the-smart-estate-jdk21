package com.sfd.thesmartestate.lms.services;


import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.responsemapper.ResponseDto;
import com.sfd.thesmartestate.lms.dto.*;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.notifications.entities.OneTimePassword;

import java.util.Collection;
import java.util.List;

public interface LeadService {
    List<Lead> findAll();

    Lead create(Lead lead);

    Lead findById(Long id);

    void delete(Lead lead);

    void save(Lead lead);

    void saveAll(Collection<Lead> leads);

    Lead findByCustomerPhoneAndProjectName(String phone, String projectName);

    List<Lead> findByCustomerPhoneAndProjectId(String phone, Long projectName);

    TransferLeadResponse transferLeads(TransferLeadDto transferLeadDto, LeadEvents leadEvent);

    PageableLeadsRespone findPageableAll(PageableFilterDto pageableFilterDto);

    Lead deactivateLead(DeactivateLeadDto deactivateLeadDto, Long leadId);

    Long findAllCount();

    Long findCountByAllActiveStatus();

    ResponseDto generateOtpToDownloadLeads();

    OneTimePassword validateOtpDownloadLeadReport(String otp);
}
