package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.common.dto.MetadataResponse;
import com.sfd.thesmartestate.lms.entities.*;
import com.sfd.thesmartestate.projects.entities.InventoryStatus;


public class MetadataResponseMapper {
    private MetadataResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }
    public static MetadataResponse mapToLeadStatus(LeadStatus leadStatus) {
        MetadataResponse response = new MetadataResponse();
        response.setId(leadStatus.getId());
        response.setName(leadStatus.getName());
        response.setDescription(leadStatus.getDescription());
        return response;
    }

    public static MetadataResponse mapToDeactivationReason(DeactivationReason deactivationReason) {
        if (deactivationReason != null) {
            MetadataResponse response = new MetadataResponse();
            response.setId(deactivationReason.getId());
            response.setName(deactivationReason.getName());
            response.setDescription(deactivationReason.getDescription());
            return response;
        }
        return null;
    }

    public static MetadataResponse mapToLeadSource(LeadSource leadSource) {
        MetadataResponse response = new MetadataResponse();
        response.setId(leadSource.getId());
        response.setName(leadSource.getName());
        response.setDescription(leadSource.getDescription());
        return response;
    }

    public static MetadataResponse mapToLeadInventorySizes(LeadInventorySize leadInventorySize) {
        MetadataResponse response = new MetadataResponse();
        response.setId(leadInventorySize.getId());
        response.setName(leadInventorySize.getSize());
        response.setDescription(leadInventorySize.getDescription());
        return response;
    }

    public static MetadataResponse mapToLeadType(LeadType leadType) {
        MetadataResponse response = new MetadataResponse();
        response.setId(leadType.getId());
        response.setName(leadType.getName());
        response.setDescription(leadType.getDescription());
        return response;
    }

    public static MetadataResponse mapToInventoryStatus(InventoryStatus inventoryStatus) {
        MetadataResponse response = new MetadataResponse();
        response.setId(inventoryStatus.getId());
        response.setName(inventoryStatus.getName());
        response.setDescription(inventoryStatus.getDescription());
        return response;
    }

}
