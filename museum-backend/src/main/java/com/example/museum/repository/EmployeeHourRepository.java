package com.example.museum.repository;

import com.example.museum.model.EmployeeHour;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface EmployeeHourRepository extends CrudRepository<EmployeeHour, Integer> {
    // DAO implementation here
    // using employeeHourID to find EmployeeHour object
    EmployeeHour findEmployeeHourByEmployeeHourID(int id);
}
