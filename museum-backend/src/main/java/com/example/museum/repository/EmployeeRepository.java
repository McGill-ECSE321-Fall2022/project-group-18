package com.example.museum.repository;

import com.example.museum.model.Employee;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

// extending the Person Repository to read the attributes of Person
// extending the Crud Repository to write the Employee
@Transactional
public interface EmployeeRepository extends PersonRepository<Employee>, CrudRepository<Employee, Integer> {
    
}