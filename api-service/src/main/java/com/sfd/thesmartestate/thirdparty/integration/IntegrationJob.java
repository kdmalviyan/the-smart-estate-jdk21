package com.sfd.thesmartestate.thirdparty.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.sfd.thesmartestate.thirdparty.integration.config.EndPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IntegrationJob {
    private final IntegrationService service;
    private final EndPointService endPointService;
    // @Scheduled(cron = "0 0/1  * * * *") // every 10 min

     // @Scheduled(cron = "0 0 06 * * *") // 8AM every day
    public void runFetchLeadJobs() {
         endPointService.findAllActive().forEach(endPoint -> {
                 JsonNode response = service.getBackendResponse(endPoint);
                 service.createLead(response, endPoint);
         });
    }
}
