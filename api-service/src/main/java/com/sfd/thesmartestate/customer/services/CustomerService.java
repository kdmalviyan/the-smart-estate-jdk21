package com.sfd.thesmartestate.customer.services;

import com.sfd.thesmartestate.customer.entities.Customer;

import java.util.Collection;
import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    Customer create(Customer customer);

    void saveAll(Collection<Customer> customers);

    Customer findByPhone(String phone);

    Customer update(Customer customer);
}
