package com.sfd.thesmartestate.projects.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "inventory")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class InventoryController {
    private final InventoryService service;
}
