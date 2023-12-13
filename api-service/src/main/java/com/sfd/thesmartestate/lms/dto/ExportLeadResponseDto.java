package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.lms.followups.FollowupDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(setterPrefix = "with")
@SuppressFBWarnings("EI_EXPOSE_REP")
public class ExportLeadResponseDto {

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String projectName;
    private String leadSource;
    private String adminRemark;
    private String status;
    private String assignTo;
    private String followupDate;
    private LocalDateTime leadUploadDate;
    private String budget;

    public static ExportLeadResponseDto buildWithLeadResponseDto(LeadResponseDto lead) {
        List<CommentDto> comments = lead.getComments().stream()
                .filter(comment -> (comment.getCommentType().equalsIgnoreCase("Remark") || comment.getCommentType().equalsIgnoreCase("Inquiry")))
                .collect(Collectors.toList());

        List<FollowupDto> followupDtos = new ArrayList<>(lead.getFollowups());

        String budget = "OL-OL";
        if (lead.getBudget() != null) {
            budget = lead.getBudget().getStartAmount() + " " + lead.getBudget().getStartUnit() + "-" + lead.getBudget().getEndAmount() + " " + lead.getBudget().getEndUnit();
        }

        return ExportLeadResponseDto.builder().withLeadSource(lead.getSource().getDescription())
                .withCustomerEmail(lead.getCustomer().getEmail())
                .withCustomerName(lead.getCustomer().getName())
                .withCustomerPhone(lead.getCustomer().getPhone())
                .withProjectName(lead.getProject().getName())
                .withAdminRemark(!comments.isEmpty() ? comments.get(0).getMessage() : "")
                .withStatus(lead.getStatus().getDescription())
                .withAssignTo(lead.getAssignedTo().getName())
                .withFollowupDate(!followupDtos.isEmpty() ? followupDtos.get(0).getFollowupTime().toString() : "")
                .withLeadUploadDate(lead.getCreatedAt())
                .withBudget(budget)
                .build();
    }
}
