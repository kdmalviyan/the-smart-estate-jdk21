package com.sfd.thesmartestate.lms.mobile;


import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.responsemapper.FollowupMapper;
import com.sfd.thesmartestate.common.responsemapper.MobileResponseDto;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.followups.FollowupRequestDto;
import com.sfd.thesmartestate.lms.followups.FollowupService;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "mobile/followup")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class MobileFollowupController {
    private final LeadService leadService;
    private final FollowupService followupService;
    private final LeadStatusService leadStatusService;
    private final LeadUpdateService leadUpdateService;

    @PostMapping("/{leadId}")
    public ResponseEntity<MobileResponseDto> create(@RequestBody String followupTime, @PathVariable("leadId") Long leadId) {
        Lead lead = leadService.findById(leadId);
        Followup followup = followupService.create(followupTime, lead);
        lead.setStatus(leadStatusService.findByName(Constants.FOLLOW_UP));
       // leadUpdateService.update(lead, LeadEvents.FOLLOWUP_STATUS_CHECK);
        leadUpdateService.updateLeadFollowup(lead, followup);


        return ResponseEntity.ok(MobileResponseDto.builder().message("Success").result("Data updated").resultData(FollowupMapper.mapToFollowup(followup)).build());
    }

    @PutMapping("/{leadId}")
    public ResponseEntity<MobileResponseDto> updateFollowupMobile(@RequestBody FollowupRequestDto followupRequestDto, @PathVariable("leadId") Long leadId) {

        Lead lead = leadService.findById(leadId);
        Followup followup = followupService.findById(followupRequestDto.getFollowUpId());
        assert Objects.nonNull(lead);

        followup.setLead(lead);
        followup.setOpen(false);
        //update follow up
        Followup followUpSaved = followupService.update(followup);
        followup.setFollowupMessage(followupRequestDto.getFollowupMessage());
        followupService.updateLeadStatus(followup);

        return ResponseEntity.ok(MobileResponseDto
                .builder()
                .message("Follow-up updated")
                .result("Success")
                .resultData(FollowupMapper.mapToFollowup(followUpSaved)).build());
    }
}
