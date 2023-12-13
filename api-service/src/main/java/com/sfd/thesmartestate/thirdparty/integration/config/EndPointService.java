package com.sfd.thesmartestate.thirdparty.integration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndPointService {
    private final EndPointRepository repository;
    public EndPoint create(EndPoint endPoint) {
        endPoint.validate();
        return repository.save(endPoint);
    }

    public Set<EndPoint> findAllActive() {
        return repository.findAllActive();
    }

    public List<EndPoint> findAll() {
        return repository.findAll();
    }

    public EndPoint update(EndPoint endPoint) {
        endPoint.validate();
        return repository.save(endPoint);
    }
}
