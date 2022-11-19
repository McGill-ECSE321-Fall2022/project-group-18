package com.example.museum.service.utils;

import com.example.museum.model.*;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.EmployeeRepository;
import com.example.museum.repository.OwnerRepository;

public class ServiceUtils {
    //check for conflicting usernames (if two users don't have the same id)
    public static boolean conflictingUsername(String username, int id, CustomerRepository customerRepository,
            EmployeeRepository employeeRepository, OwnerRepository ownerRepository) {
        for (Customer c : customerRepository.findAll()) {
            if (c.getUsername().equals(username) && c.getAccountID() != id)
                return true;
        }
        for (Employee e : employeeRepository.findAll()) {
            if (e.getUsername().equals(username) && e.getAccountID() != id)
                return true;
        }
        for (Owner o : ownerRepository.findAll()) {
            if (o.getUsername().equals(username) && o.getAccountID() != id)
                return true;
        }
        return false;
    }
}
