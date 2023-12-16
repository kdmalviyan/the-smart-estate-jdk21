package com.sfd.thesmartestate.customer.services;

import com.sfd.thesmartestate.common.services.RoleService;
import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.customer.repositories.CustomerRepository;
import com.sfd.thesmartestate.users.entities.LoginDetails;
import com.sfd.thesmartestate.users.services.LoginDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final LoginDetailsService loginDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Customer create(Customer customer) {
        LoginDetails loginDetails = loginDetailsService
                .findByUsername(customer.getLoginDetails().getUsername());

        if(Objects.isNull(loginDetails)){
            String customerUniqueId = UUID.randomUUID().toString();
            customer.setCustomerUniqueId(customerUniqueId);
            customer.getLoginDetails().setCustomerUniqueId(customerUniqueId);
            customer.getLoginDetails()
                    .setPassword(passwordEncoder.encode(customer.getLoginDetails().getPassword()));
            customer.getLoginDetails().setRoles(Set.of(roleService.findByName("ROLE_CUSTOMER")));
            return repository.saveAndFlush(customer);
        }
        return repository.findByCustomerUniqueId(loginDetails.getCustomerUniqueId());
    }

    @Override
    public void saveAll(Collection<Customer> customers) {
        repository.saveAll(customers);
    }

    @Override
    public Customer findByPhone(String phone) {
        return repository.findByPhone(phone).orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        return repository.saveAndFlush(customer);
    }

    @Override
    public Customer findByCustomerUniqueId(String customerUniqueId) {
        return repository.findByCustomerUniqueId(customerUniqueId);
    }

}
