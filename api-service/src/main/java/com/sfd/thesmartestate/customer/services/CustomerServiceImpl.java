package com.sfd.thesmartestate.customer.services;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.customer.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void saveAll(Collection<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    @Override
    public Customer findByPhone(String phone) {
        return customerRepository.findByPhone(phone).orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

}
