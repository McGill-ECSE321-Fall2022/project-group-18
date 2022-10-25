package com.example.museum.repository;

import com.example.museum.model.Employee;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends PersonRepository<Employee>, CrudRepository<Employee, Integer> {
    
}