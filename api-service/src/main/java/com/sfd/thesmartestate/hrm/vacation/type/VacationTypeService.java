package com.sfd.thesmartestate.hrm.vacation.type;

import java.util.List;

public interface VacationTypeService {
    VacationType create(VacationType vacationType);
    VacationType update(VacationType vacationType);
    List<VacationType> findAll();
    VacationType findById(Integer id);
    void delete(Integer id);

    List<VacationType> findAllActive();
}
