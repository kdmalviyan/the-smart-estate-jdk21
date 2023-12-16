package com.sfd.thesmartestate.hrm.vacation.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VacationActionServiceImpl implements VacationActionService {
    private final VacationActionRepository repository;

    @Override
    @Transactional
    public VacationAction create(VacationAction vacationAction) {
        vacationAction.validate();
        return repository.save(vacationAction);
    }

    @Override
    public List<VacationAction> findByUserId(Long userId) {
        return repository.userId(userId);
    }

    @Override
    public VacationAction findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<VacationAction> findByVacationId(Long vacationId) {
        return repository.findByVacationId(vacationId);
    }

    @Override
    public List<VacationAction> findByVacationIdAndUserId(Long vacationId, Long userId) {
        return repository.findByVacationIdAndUserId(vacationId, userId);
    }
}
