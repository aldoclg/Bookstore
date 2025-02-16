package com.bookstore.infrastructure.dataproviders;

import com.bookstore.application.gateways.CustomerGateway;
import com.bookstore.application.models.Customer;
import com.bookstore.infrastructure.dataproviders.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerProvider implements CustomerGateway {

    private CustomerRepository repository;

    @Override
    public Optional<Customer> getCustomer(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void save(Customer customer) {
        repository.save(customer);
    }
}
