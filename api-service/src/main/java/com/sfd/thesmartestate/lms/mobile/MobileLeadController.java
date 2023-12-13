package com.sfd.thesmartestate.lms.mobile;


import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.responsemapper.MobileResponseDto;
import com.sfd.thesmartestate.lms.dto.DeactivateLeadDto;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.entities.LeadStatus;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "mobile/lead")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class MobileLeadController {
    private final LeadService leadService;
    private final LeadStatusService leadStatusService;
    private final LeadUpdateService leadUpdateService;


    @PutMapping(value = "/{leadId}/status/{statusId}")
    public ResponseEntity<Lead> update(@PathVariable("leadId") Long leadId, @PathVariable("statusId") Long statusId,
                                       @RequestParam(name = "leadUpdateEvent") LeadEvents leadEvent) {
        Lead lead = leadService.findById(leadId);
        LeadStatus leadStatus = leadStatusService.findById(statusId);
        lead.setStatus(leadStatus);
        return ResponseEntity.ok(leadUpdateService.update(lead, leadEvent));
    }

    @PutMapping(value = "/{leadId}/siteVisit/{siteVisit}")
    public ResponseEntity<Lead> updateSiteVisit(@PathVariable("leadId") Long leadId, @PathVariable("siteVisit") Boolean siteVisit,
                                                @RequestParam(name = "leadUpdateEvent") LeadEvents leadEvent) {
        Lead lead = leadService.findById(leadId);
        lead.setSiteVisit(siteVisit);
        return ResponseEntity.ok(leadUpdateService.update(lead, leadEvent));
    }

    @PostMapping(value = "/deactivateLead/{leadId}")
    public ResponseEntity<MobileResponseDto> deactivateLead(@RequestBody DeactivateLeadDto deactivateLeadDto, @PathVariable("leadId") Long leadId) {
        Lead lead = leadService.deactivateLead(deactivateLeadDto, leadId);
        return ResponseEntity.ok(MobileResponseDto.builder().message("Success").result("Data updated").resultData(lead).build());
    }

}
