package com.sfd.thesmartestate.lms.followups;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.responsemapper.FollowupMapper;
import com.sfd.thesmartestate.lms.dto.FilterRequestPayload;
import com.sfd.thesmartestate.lms.dto.PageableFollowupResponse;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.helpers.PageableFilterRequestHelper;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("followup")
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class FollowupController {
    private final FollowupService followupService;
    private final LeadService leadService;
    private final LeadUpdateService leadUpdateService;
    private final LeadStatusService leadStatusService;
    private final PageableFilterRequestHelper pageableFilterRequestHelper;


    @GetMapping
    public ResponseEntity<List<Lead>> findAll() {
        return ResponseEntity.ok(leadService.findAll().stream()
                .filter(lead -> "follow-up".equalsIgnoreCase(lead.getStatus().getName()))
                .collect(Collectors.toList()));
    }

    @PostMapping("pageable")
    public ResponseEntity<PageableFollowupResponse> findPageableAll(@RequestBody FilterRequestPayload filterRequestPayload) {
        pageableFilterRequestHelper.validateRequestPayload(filterRequestPayload);
        return ResponseEntity.ok(followupService.findAllFollowupPageable(pageableFilterRequestHelper
                .buildPageableFilterRequest(filterRequestPayload)));
    }

    @PostMapping("/{leadId}")
    public ResponseEntity<FollowupDto> create(@RequestBody String followupTime, @PathVariable("leadId") Long leadId) {
        Lead lead = leadService.findById(leadId);
        Followup followup = followupService.create(followupTime, lead);
        lead.setStatus(leadStatusService.findByName(Constants.FOLLOW_UP));

        leadUpdateService.updateLeadFollowup(lead, followup);
        return ResponseEntity.ok(FollowupMapper.mapToFollowup(followup));
    }

    @PutMapping("/{leadId}")
    public ResponseEntity<FollowupDto> update(@RequestBody Followup followupInput, @PathVariable("leadId") Long leadId) {

        Lead lead = leadService.findById(leadId);
        Followup followup = followupService.findById(followupInput.getId());
        assert Objects.nonNull(lead);

        followup.setLead(lead);
        followup.setOpen(followupInput.isOpen());
        //update follow up
        Followup followUpSaved = followupService.update(followup);
        followup.setFollowupMessage(followupInput.getFollowupMessage());
        followupService.updateLeadStatus(followup);
        return ResponseEntity.ok(FollowupMapper.mapToFollowup(followUpSaved));
    }


}
