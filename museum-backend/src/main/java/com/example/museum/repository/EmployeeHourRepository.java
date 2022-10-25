package com.example.museum.repository;

import com.example.museum.model.EmployeeHour;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeHourRepository extends CrudRepository<EmployeeHour, Integer> {
    EmployeeHour findEmployeeHourByEmployeeHourID(int id);
}
