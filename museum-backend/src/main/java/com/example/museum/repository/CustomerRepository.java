package com.example.museum.repository;

import com.example.museum.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    public Customer findCustomerByAccountID(int id);
}
