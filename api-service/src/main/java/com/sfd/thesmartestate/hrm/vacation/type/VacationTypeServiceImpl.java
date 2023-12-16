package com.sfd.thesmartestate.hrm.vacation.type;

import com.sfd.thesmartestate.hrm.vacation.exceptions.VacationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationTypeServiceImpl implements VacationTypeService {

    private final VacationTypeRepository repository;

    @Override
    public VacationType create(VacationType vacationType) {
        vacationType.validate();
        vacationType.setCreatedAt(LocalDateTime.now());
        vacationType.setUpdatedAt(LocalDateTime.now());
        return repository.save(vacationType);
    }

    @Override
    public VacationType update(VacationType vacationType) {
        vacationType.validate();
        vacationType.setUpdatedAt(LocalDateTime.now());
        return repository.save(vacationType);
    }

    @Override
    public List<VacationType> findAll() {
        return repository.findAll();
    }

    @Override
    public VacationType findById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new VacationException("Unable to find vacation type with id" + id));
    }

    @Override
    public void delete(Integer id) {
        VacationType vacationType = findById(id);
        vacationType.setActive(false);
        update(vacationType);
    }

    @Override
    public List<VacationType> findAllActive() {
        return repository.findAllActive();
    }
}
