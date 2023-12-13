package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.DeactivationReason;

import java.util.List;


public interface DeactivationReasonService {

    DeactivationReason create(DeactivationReason deactivationReason);

    DeactivationReason update(DeactivationReason deactivationReason);

    List<DeactivationReason> findAll();

    DeactivationReason findByName(String name);

    DeactivationReason findById(Long id);

    DeactivationReason delete(Long id);

    String createDeactivationReasonName(String name);
}
