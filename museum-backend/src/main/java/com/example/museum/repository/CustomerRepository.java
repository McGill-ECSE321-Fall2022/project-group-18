package com.example.museum.repository;

import com.example.museum.model.Customer;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

// extending the Person Repository to read the attributes of Person
// extending the Crud Repository to write the Customer
@Transactional
public interface CustomerRepository extends PersonRepository<Customer>, CrudRepository<Customer, Integer> {
}
