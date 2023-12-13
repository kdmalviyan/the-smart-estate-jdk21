package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.common.responsemapper.ResponseDto;
import com.sfd.thesmartestate.lms.dto.TargetDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(value = "targets")
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TargetController {
    private final TargetService targetService;

    @PostMapping
    public ResponseEntity<TargetDto> create(@RequestBody Target target) {
        return ResponseEntity.ok(TargetDto.buildWithTarget(targetService.create(target)));
    }

    @GetMapping("/{groupBy}")
    public ResponseEntity<List<TargetDto>> findAll(@PathVariable("groupBy") boolean groupBy) {
        return ResponseEntity.ok(targetService.findAll(groupBy).stream().map(TargetDto::buildWithTarget).collect(Collectors.toList()));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteTarget(@RequestParam("id") Long targetId) {
        targetService.delete(targetId);
        return ResponseEntity.ok(ResponseDto.builder().message("Delete Successfully").build());
    }


}
