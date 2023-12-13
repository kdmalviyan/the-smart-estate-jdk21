package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.common.responsemapper.ProjectResponseMapper;
import com.sfd.thesmartestate.lms.targets.Target;
import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(setterPrefix = "with")
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TargetDto {
    private Long id;
    private String employeeName;
    private Long bookingCount;
    private Long bookingDoneCount;
    private Long siteVisitCount;
    private Long siteVisitDoneCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectDTO project;
    private Boolean isActive;

    public static TargetDto buildWithTarget(Target target) {
        return TargetDto.builder()
                .withId(target.getId())
                .withBookingCount(target.getBookingCount())
                .withBookingDoneCount(target.getBookingDoneCount())
                .withEmployeeName(target.getAssignedTo().getName())
                .withSiteVisitCount(target.getSiteVisitCount())
                .withSiteVisitDoneCount(target.getSiteVisitDoneCount())
                .withProject(ProjectResponseMapper.mapToProjectResponse(target.getProject()))
                .withStartDate(target.getStartDate())
                .withEndDate(target.getEndDate())
                .withIsActive(target.isActive())
                .build();
    }
}
