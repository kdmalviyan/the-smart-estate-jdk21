package com.sfd.thesmartestate.customer.controllers;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.customer.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "customer")
@CrossOrigin("*")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> listAll() {
        List<Customer> vendors = customerService.findAll();
        return ResponseEntity.ok(vendors);
    }

    @PostMapping("signup")
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.create(customer));
    }

    @PutMapping
    public ResponseEntity<Customer> update(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.update(customer));
    }

    @GetMapping(value = "/phone/{phoneNumber}")
    public ResponseEntity<Customer> findById(@PathVariable("phoneNumber") String phone) {
        return ResponseEntity.ok(customerService.findByPhone(phone));
    }
}
