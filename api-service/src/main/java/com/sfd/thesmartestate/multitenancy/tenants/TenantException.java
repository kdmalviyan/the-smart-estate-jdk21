package com.sfd.thesmartestate.multitenancy.tenants;

/**
 * @author kuldeep
 */
public class TenantException extends RuntimeException{
    public TenantException(String message) {
        super(message);
    }
}
