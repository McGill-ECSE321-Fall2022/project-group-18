package com.example.museum.service;

import com.example.museum.model.*;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeService(@Autowired EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) throws Exception {
        try {
            String username = employee.getUsername();
            if (!conflictingUsername(username)) {
                Employee createdEmployee = employeeRepository.save(employee);
                return createdEmployee;
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println("Please enter a new username. Current username is already taken");
        }

        return null;
    }

    private boolean conflictingUsername(String username) {
        for (Employee c : employeeRepository.findAll()) {
            if (c.getPassword().equals(username))
                return true;
        }
        return false;
    }

    public Optional<Employee> retrieveEmployee(int id) {
        return employeeRepository.findById(id);
    }
}
