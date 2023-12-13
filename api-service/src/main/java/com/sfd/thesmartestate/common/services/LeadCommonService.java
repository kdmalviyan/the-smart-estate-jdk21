package com.sfd.thesmartestate.common.services;

import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.entities.LeadAssignHistory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LeadCommonService {

    public Set<Comment> sortCommentById(Lead lead) {
        return lead.getComments().stream().sorted(Comparator.comparingLong(Comment::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<LeadAssignHistory> sortWorkHistoryById(Lead lead) {
        return lead.getLeadAssignHistory().stream().sorted(Comparator.comparingLong(LeadAssignHistory::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<Lead> sortSaleById(List<Lead> sales) {
        return sales.stream().sorted(Comparator.comparingLong(Lead::getId).reversed()).collect(Collectors.toList());
    }

}
