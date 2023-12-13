package com.sfd.thesmartestate.lms.rawleads;

import com.sfd.thesmartestate.lms.dto.FilterRequestPayload;
import com.sfd.thesmartestate.lms.dto.PageableRawLeadsResponse;
import com.sfd.thesmartestate.lms.helpers.PageableFilterRequestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "lead/raw")
@CrossOrigin("*")
public class RawLeadController {
    private final RawLeadService rawLeadService;
    private final PageableFilterRequestHelper pageableFilterRequestHelper;

    @GetMapping
    public ResponseEntity<List<RawLead>> findAll() {
        return ResponseEntity.ok(rawLeadService.getAll());
    }

    @PostMapping("/pageableLeads")
    public ResponseEntity<PageableRawLeadsResponse> findPageableAll(@RequestBody FilterRequestPayload filterRequestPayload) {
        pageableFilterRequestHelper.validateRequestPayload(filterRequestPayload);

        PageableRawLeadsResponse pageableLeadsRespone = rawLeadService
                .findPageableAll(pageableFilterRequestHelper.buildPageableFilterRequestForRawLead(filterRequestPayload));
        return ResponseEntity.ok(pageableLeadsRespone);
    }

}
