package com.sfd.thesmartestate.thirdparty.integration.remoteleads;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "lead/remote")
@CrossOrigin("*")
public class RemoteLeadController {
    private final RemoteLeadService service;
    public ResponseEntity<List<RemoteLead>> findAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
