package com.sfd.thesmartestate.multitenancy.tenants;

import lombok.Builder;
import lombok.Data;

/**
 * @author kuldeep
 */
@Data
@Builder(setterPrefix = "with")
public class TenantExceptionResponse {
    private String message;
    private int statusCode;
}
