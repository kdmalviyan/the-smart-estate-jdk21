package com.sfd.thesmartestate.adhoc.folloups;/*
package com.ccs.realestate.adhoc.folloups;

import entities.lms.com.sfd.thesmartestate.Lead;
import followups.lms.com.sfd.thesmartestate.Followup;
import followups.lms.com.sfd.thesmartestate.FollowupRepository;
import com.ccs.realestate.lms.followups.entities.Followup;
import com.ccs.realestate.lms.followups.repositories.FollowupRepository;
import com.ccs.realestate.lms.followups.services.FollowupService;
import repositories.lms.com.sfd.thesmartestate.LeadRepository;
import services.lms.com.sfd.thesmartestate.LeadService;
import entities.projects.com.sfd.thesmartestate.Project;
import entities.users.com.sfd.thesmartestate.User;
import services.users.com.sfd.thesmartestate.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowupRemappingRepository {
    private final LeadRepository leadRepository;
    private final FollowupRepository followupRepository;

    public void updateFollowupMappingToLeads(String requestId) {
        List<Lead> leads = leadRepository.findAll();
        for (Lead lead : leads) {
            Set<Followup> followups = lead.getFollowups();
            for (Followup followup : followups) {
                log.info("Request id " + requestId + " Updating follouwup " + followup.getId() + " For lead " + lead.getId());
                followup.setLead(lead);
                followupRepository.save(followup);
            }
        }
    }
}
*/
