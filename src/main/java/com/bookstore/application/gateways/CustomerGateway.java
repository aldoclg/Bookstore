package com.bookstore.application.gateways;

import com.bookstore.application.models.Customer;

import java.util.Optional;

public interface CustomerGateway {
    Optional<Customer> getCustomer(Long id);

    boolean existsById(Long id);

    void save(Customer customer);
}
