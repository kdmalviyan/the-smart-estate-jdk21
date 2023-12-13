package com.sfd.thesmartestate.lms.controllers;


import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.responsemapper.LeadResponseMapper;
import com.sfd.thesmartestate.common.responsemapper.ResponseDto;
import com.sfd.thesmartestate.lms.dto.*;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.followups.FollowupService;
import com.sfd.thesmartestate.lms.helpers.PageableFilterRequestHelper;
import com.sfd.thesmartestate.lms.rawleads.RawLead;
import com.sfd.thesmartestate.lms.rawleads.RawLeadService;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "lead")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class LeadController {
    private final LeadService leadService;
    private final LeadUpdateService leadUpdateService;
    private final RawLeadService rawLeadService;
    private final FollowupService followupService;
    private final PageableFilterRequestHelper pageableFilterRequestHelper;

    @GetMapping
    public ResponseEntity<List<Lead>> findAll() {
        return ResponseEntity.ok(leadService.findAll());
    }

    @PostMapping("/pageableLeads")
    public ResponseEntity<PageableLeadsRespone> findPageableAll(@RequestBody FilterRequestPayload filterRequestPayload) {
        pageableFilterRequestHelper.validateRequestPayload(filterRequestPayload);
        PageableLeadsRespone pageableLeadsRespone = leadService
                .findPageableAll(pageableFilterRequestHelper.buildPageableFilterRequest(filterRequestPayload));
        return ResponseEntity.ok(pageableLeadsRespone);
    }

    @PostMapping("/pageableLeads/export")
    public ResponseEntity<List<ExportLeadResponseDto>> exportLeads(@RequestBody FilterRequestPayload filterRequestPayload) {
        pageableFilterRequestHelper.validateRequestPayload(filterRequestPayload);
        PageableLeadsRespone pageableLeadsRespone = leadService
                .findPageableAll(pageableFilterRequestHelper.buildPageableFilterRequest(filterRequestPayload));
        List<ExportLeadResponseDto> exportLeadResponseDto = pageableLeadsRespone.getLeads().stream().map(ExportLeadResponseDto::buildWithLeadResponseDto).collect(Collectors.toList());
        return ResponseEntity.ok(exportLeadResponseDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<LeadResponseDto> findById(@PathVariable("id") Long id) {
        Lead lead = leadService.findById(id);
        return ResponseEntity.ok(LeadResponseMapper.mapToLeadResponse(lead, followupService.findFollowupByLead(lead)));
    }

    @PostMapping
    public ResponseEntity<Lead> create(@RequestBody Lead lead) {
        return ResponseEntity.ok(leadService.create(lead));
    }

    @PostMapping("/rawlead/{rawId}")
    public ResponseEntity<RawLead> createRawLeadToLead(@RequestBody Lead lead, @PathVariable("rawId") Long rawId) {
        leadService.create(lead);
        RawLead rawLead=rawLeadService.updateConvertedLeadStatus(rawId);
        return ResponseEntity.ok(rawLead);
    }

    @PostMapping("/transferLeads")
    public ResponseEntity<TransferLeadResponse> transferLead(@RequestBody TransferLeadDto transferLeadDto, @RequestParam(name = "leadUpdateEvent") LeadEvents leadEvent) {
        return ResponseEntity.ok(leadService.transferLeads(transferLeadDto, leadEvent));
    }

    @PutMapping
    public ResponseEntity<Lead> update(@RequestBody Lead lead, @RequestParam(name = "leadUpdateEvent") LeadEvents leadEvent) {
        return ResponseEntity.ok(leadUpdateService.update(lead, leadEvent));
    }

    @PutMapping(value = "/changeproject")
    public ResponseEntity<Lead> updateProject(@RequestBody ProjectChangeDto projectChangeDto) {
        return ResponseEntity.ok(leadUpdateService.updateProject(projectChangeDto.getLeadId(), projectChangeDto.getProjectId(), projectChangeDto.getUserId()));
    }

    @PostMapping(value = "/deactivateLead/{leadId}")
    public ResponseEntity<Lead> deactivateLead(@RequestBody DeactivateLeadDto deactivateLeadDto, @PathVariable("leadId") Long leadId) {
        return ResponseEntity.ok(leadService.deactivateLead(deactivateLeadDto, leadId));
    }

    @GetMapping("/downloadFileOtp")
    public ResponseEntity<ResponseDto> generateOtpFileDownload() {
        return ResponseEntity.ok(leadService.generateOtpToDownloadLeads());
    }

    @GetMapping("/validateDownloadFileOtp/{otp}")
    public ResponseEntity<OneTimePassword> validateOtpFileDownload(@PathVariable("otp") String otp) {
        return ResponseEntity.ok(leadService.validateOtpDownloadLeadReport(otp));
    }

}
