package com.example.museum.repository;

import com.example.museum.model.Customer;
import javax.transaction.Transactional;

@Transactional
public interface CustomerRepository extends PersonAbsRepository<Customer> {
}
