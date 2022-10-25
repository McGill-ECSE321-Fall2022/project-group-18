package com.example.museum.repository;

import com.example.museum.model.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Employee findEmployeeByAccountID(int id);
}
