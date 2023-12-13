package com.sfd.thesmartestate.customer.repositories;

import com.sfd.thesmartestate.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhone(String number);
}
