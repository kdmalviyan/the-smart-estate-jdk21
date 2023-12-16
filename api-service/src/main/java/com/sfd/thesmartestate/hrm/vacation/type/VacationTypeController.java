package com.sfd.thesmartestate.hrm.vacation.type;

import com.sfd.thesmartestate.common.responsemapper.ResponseDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vacation/type")
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class VacationTypeController {

    private final VacationTypeService vacationTypeService;

    @PostMapping("")
    @Secured("ROLE_SUPERADMIN")
    public ResponseEntity<VacationType> create(@RequestBody VacationType vacationType) {
        return ResponseEntity.ok(vacationTypeService.create(vacationType));
    }

    @PutMapping("")
    @Secured("ROLE_SUPERADMIN")
    public ResponseEntity<VacationType> update(@RequestBody VacationType vacationType) {
        return ResponseEntity.ok(vacationTypeService.update(vacationType));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_SUPERADMIN")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        vacationTypeService.delete(id);
        return ResponseEntity.ok(ResponseDto.builder().status(200L).message("Deleted Successfully.").build());
    }

    @GetMapping("")
    public ResponseEntity<List<VacationType>> findAll() {
        return ResponseEntity.ok(vacationTypeService.findAllActive());
    }
}
