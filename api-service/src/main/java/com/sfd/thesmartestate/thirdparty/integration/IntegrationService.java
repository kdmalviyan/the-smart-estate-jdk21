package com.sfd.thesmartestate.thirdparty.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.sfd.thesmartestate.thirdparty.integration.config.EndPoint;
import com.sfd.thesmartestate.thirdparty.integration.remoteleads.RemoteLead;

public interface IntegrationService {

    JsonNode getBackendResponse(EndPoint endPoint);

    void createLead(JsonNode response, EndPoint endPoint);

    void createRemoteLead(RemoteLead remoteLeadDto);
}
