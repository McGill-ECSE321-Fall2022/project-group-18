package com.example.museum.service;

import com.example.museum.model.*;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {
    private OwnerRepository ownerRepository;
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;

    public OwnerService(@Autowired OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner createOwner(Owner owner) throws Exception {
        try {
            String username = owner.getUsername();
            if (!conflictingUsername(username)) {
                Owner createdOwner = ownerRepository.save(owner);
                return createdOwner;
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println("Please enter a new username. Current username is already taken");
        }

        return null;
    }

    private boolean conflictingUsername(String username) {
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

    public Optional<Owner> retrieveOwner(int id) {
        return ownerRepository.findById(id);
    }
}
