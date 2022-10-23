package com.example.museum.repository;

import com.example.museum.model.Customer;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface CustomerRepository extends PersonRepository<Customer>, CrudRepository<Customer, Integer> {
}
