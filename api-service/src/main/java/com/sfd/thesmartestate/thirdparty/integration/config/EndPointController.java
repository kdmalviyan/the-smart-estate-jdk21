package com.sfd.thesmartestate.thirdparty.integration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/thirdparty/endpoint")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EndPointController {
    private final EndPointService endPointService;

    @PostMapping("")
    public ResponseEntity<EndPoint> createEndpoint(@RequestBody EndPoint endPoint) {
        return ResponseEntity.ok(endPointService.create(endPoint));
    }

    @PutMapping("")
    public ResponseEntity<EndPoint> update(@RequestBody EndPoint endPoint) {
        return ResponseEntity.ok(endPointService.update(endPoint));
    }

    @GetMapping("")
    public ResponseEntity<List<EndPoint>> listAll() {
        return ResponseEntity.ok(endPointService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<Set<EndPoint>> listActive() {
        return ResponseEntity.ok(endPointService.findAllActive());
    }
}
