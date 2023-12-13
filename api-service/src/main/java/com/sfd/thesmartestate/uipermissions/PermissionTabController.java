package com.sfd.thesmartestate.uipermissions;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "permissionTabs")
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class PermissionTabController {

    private final PermissionTabService permissionTabService;

    @GetMapping("")
    private ResponseEntity<?> getAll() {
        return ResponseEntity.ok(permissionTabService.findAll());
    }

    @GetMapping("topMenuOnly")
    private ResponseEntity<?> getTopAllWithSubmenu() {
        return ResponseEntity.ok(permissionTabService.findAll().stream().filter(PermissionTab::isTopMenu).collect(Collectors.toList()));
    }

    @PostMapping("addPermission/{roleName}/{addRemove}")
    private ResponseEntity<?> get(@RequestBody PermissionTab permissionTab, @PathVariable("roleName") String roleName, @PathVariable("addRemove") String addRemove) {
        return ResponseEntity.ok(permissionTabService.addPermissionToRole(permissionTab, roleName, addRemove));
    }

    @PostMapping("")
    private ResponseEntity<PermissionTab> createMenu(
            @RequestBody PermissionTab permissionTab,
            @RequestParam(value = "parentModule", required = false) String parentModuleName) {
        return ResponseEntity.ok(permissionTabService.addPermission(permissionTab, parentModuleName));
    }
}
