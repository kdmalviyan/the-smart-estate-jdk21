package com.sfd.thesmartestate.thirdparty.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sfd.thesmartestate.thirdparty.exceptions.RemoteServiceNotAvailableException;
import com.sfd.thesmartestate.thirdparty.integration.config.AuthRequestHeader;
import com.sfd.thesmartestate.thirdparty.integration.config.EndPoint;
import com.sfd.thesmartestate.thirdparty.integration.config.EndpointQueryParam;
import com.sfd.thesmartestate.thirdparty.integration.config.LeadFieldsMapper;
import com.sfd.thesmartestate.thirdparty.integration.remoteleads.RemoteLead;
import com.sfd.thesmartestate.thirdparty.integration.remoteleads.RemoteLeadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {
    private final RemoteLeadService remoteLeadService;
    private final AtomicInteger retryCount = new AtomicInteger(0);

    @Override
    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public JsonNode getBackendResponse(final EndPoint endPoint) {
        try {
            log.info("Getting leads from " + endPoint.getName());
            log.info("Retry count " + retryCount.incrementAndGet());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> response = getResponse(endPoint, restTemplate);
            log.info("Retry count reset " + retryCount.get());
            return response.getBody();
        } catch (Exception e) {
            throw new RemoteServiceNotAvailableException(e.getMessage());
        } finally {
            retryCount.set(0);
        }
    }

    @Override
    public void createLead(JsonNode response, EndPoint endPoint) {
        Set<LeadFieldsMapper> leadFieldsMappers = endPoint.getFieldsMappers();
        Collection<RemoteLead> leads = new HashSet<>();
        if (response.isArray()) {
            if (response.size() > 0) {
                log.info(response.size() + " leads received.");
                for (JsonNode leadJson : response) {
                    RemoteLead lead = new RemoteLead();
                    for (LeadFieldsMapper mapper : leadFieldsMappers) {
                        String leadKey = mapper.getPropKey();
                        String mappingKey = mapper.getPropValue();
                        String value = leadJson.has(mappingKey) ? leadJson.get(mappingKey).asText() : "";
                        setLeadValue(lead, leadKey, value);
                    }

                    RemoteLead existingLead = remoteLeadService.findByCustomerPhoneAndProjectName(lead.getCustomerPhone(), lead.getProjectName());
                    if (Objects.nonNull(existingLead)) {
                        log.info("Lead already exist." + leadJson);
                        continue;
                    }
                    leads.add(lead);
                }
            } else {
                log.warn("No lead received.");
            }
        } else {
            log.error("Invalid response received: " + response);
        }
        remoteLeadService.saveAll(leads);
    }

    @Override
    public void createRemoteLead(RemoteLead remoteLead) {
        remoteLead.validate();
        remoteLeadService.create(remoteLead);
    }

    private ResponseEntity<JsonNode> getResponse(EndPoint endPoint, RestTemplate restTemplate) {
        return restTemplate.exchange
                (createCompleteUrl(endPoint), endPoint.getHttpMethod(), createHttpEntry(endPoint), JsonNode.class);
    }

    private HttpEntity<JsonNode> createHttpEntry(EndPoint endPoint) {
        HttpEntity<JsonNode> httpEntity;
        if (AuthenticationType.BASIC.equals(endPoint.getAuthType())) {
            httpEntity = new HttpEntity<>(createBasicAuthHeaders(endPoint));
        } else if (AuthenticationType.TOKEN_BASED.equals(endPoint.getAuthType())) {
            httpEntity = new HttpEntity<>(createTokenAuthHeaders(endPoint));
        } else {
            httpEntity = new HttpEntity<>(createSimpleHeaders(endPoint, new HttpHeaders()));
        }
        return httpEntity;
    }

    private HttpHeaders createSimpleHeaders(EndPoint endPoint, HttpHeaders httpHeaders) {
        for (AuthRequestHeader header : endPoint.getHeaders()) {
            httpHeaders.set(header.getHeaderKey(), header.getHeaderValue());
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private HttpHeaders createTokenAuthHeaders(EndPoint endPoint) {
        String accessToken = getBearerToken(endPoint);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        createSimpleHeaders(endPoint, headers);
        return headers;
    }

    private String getBearerToken(EndPoint endPoint) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put(endPoint.getAccessTokenUsernameFieldName(), endPoint.getUsername());
            requestBody.put(endPoint.getAccessTokenPasswordFieldName(), endPoint.getPassword());
            ResponseEntity<JsonNode> responseEntityStr = restTemplate.
                    postForEntity(endPoint.getTokenUrl(), requestBody, JsonNode.class);
            return Objects.requireNonNull(responseEntityStr.getBody()).get(endPoint.getAccessTokenResponseFieldName()).asText();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get access token: " + e.getMessage());
        }
    }

    private static String createCompleteUrl(EndPoint endPoint) {
        StringBuilder urlBuilder = new StringBuilder(endPoint.getUrl()).append("?");
        for (EndpointQueryParam queryParam : endPoint.getParams()) {
            urlBuilder.append(queryParam.getParamKey())
                    .append("=")
                    .append(EncryptionService.apply(queryParam)).
                    append("&");
        }
        log.info("Remote url: " + urlBuilder);
        return urlBuilder.length() > 2
                ? urlBuilder.substring(0, urlBuilder.lastIndexOf("&"))
                : "";
    }


    HttpHeaders createBasicAuthHeaders(EndPoint endPoint) {
        HttpHeaders headers = new HttpHeaders() {{
            String auth = endPoint.getUsername() + ":" + endPoint.getPassword();
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth, StandardCharsets.US_ASCII);
            set("Authorization", authHeader);
        }};
        createSimpleHeaders(endPoint, headers);
        return headers;
    }

    private void setLeadValue(RemoteLead lead, String leadKey, String value) {
        switch (leadKey){
            case "customerName":
                lead.setCustomerName(value);
                break;
            case "customerPhone":
                lead.setCustomerPhone(value);
                break;
            case "customerEmail":
                lead.setCustomerEmail(value);
                break;
            case "projectId":
                // TODO:
                break;
            case "projectName":
                lead.setProjectName(value);
                break;
            case "locality":
                // TODO:
                break;
            case "leadDate":
                LocalDateTime date =
                        Instant.ofEpochMilli(Long.parseLong(value))
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                lead.setCreatedAt(date);
                lead.setUpdatedAt(date);
                lead.setLeadDate(date.toLocalDate());
                break;
            default:
                log.info("No matching mapping exists " + value);

        }
    }
}
