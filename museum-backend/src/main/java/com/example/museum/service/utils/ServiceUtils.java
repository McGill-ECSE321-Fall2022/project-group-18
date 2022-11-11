package com.example.museum.service.utils;

import com.example.museum.model.*;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.EmployeeRepository;
import com.example.museum.repository.OwnerRepository;

public class ServiceUtils {
    public static boolean conflictingUsername(String username, CustomerRepository customerRepository,
            EmployeeRepository employeeRepository, OwnerRepository ownerRepository) {
        for (Customer c : customerRepository.findAll()) {
            if (c.getPassword().equals(username))
                return true;
        }
        for (Employee e : employeeRepository.findAll()) {
            if (e.getPassword().equals(username))
                return true;
        }
        for (Owner o : ownerRepository.findAll()) {
            if (o.getPassword().equals(username))
                return true;
        }
        return false;
    }
}
