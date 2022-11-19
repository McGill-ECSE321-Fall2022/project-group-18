package com.example.museum.service;

import com.example.museum.dto.OwnerDto;
import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.*;
import com.example.museum.repository.*;
import com.example.museum.service.utils.ServiceUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Owner getOwnerByID(int id) {
        Owner owner = ownerRepository.findByAccountID(id);
        if (owner == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Owner not found");
        }

        return owner;
    }

    @Transactional
    public void loginOwner(Owner ownerRequest) {
        Iterator<Owner> owners = ownerRepository.findAll().iterator();

        while (owners.hasNext()) {
            Owner owner = owners.next();
            if (owner.getUsername().equals(ownerRequest.getUsername())) {
                if (owner.getPassword().equals(ownerRequest.getPassword())) {
                    return;
                } else {
                    throw new RequestException(HttpStatus.BAD_REQUEST, "Wrong password");
                }
            }
        }
        throw new DatabaseException(HttpStatus.NOT_FOUND, "Owner does not exist");
    }

    @Transactional
    public Owner createOwner(Owner ownerRequest) {
        if (ownerRepository.findByAccountID(ownerRequest.getAccountID()) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "An owner with the given id already exists.");
        }else if(ServiceUtils.conflictingUsername(ownerRequest.getUsername(), customerRepository, employeeRepository,
                ownerRepository)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "An owner with the given username already exists.");
        }
        Owner owner = ownerRepository.save(ownerRequest);
        return owner;
    }

    // private boolean conflictingUsername(String username) {
    // for (Customer c : customerRepository.findAll()) {
    // if (c.getPassword().equals(username))
    // return true;
    // }
    // for (Employee e : employeeRepository.findAll()) {
    // if (e.getPassword().equals(username))
    // return true;
    // }
    // for (Owner o : ownerRepository.findAll()) {
    // if (o.getPassword().equals(username))
    // return true;
    // }
    // return false;
    // }

    public Owner modifyOwnerByID(int id, String username, String password, String firstName, String lastName) {
        Owner owner = ownerRepository.findByAccountID(id);
        if (owner == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Owner not found");
        }else if (ServiceUtils.conflictingUsername(username, customerRepository, employeeRepository, ownerRepository)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "A user with the given username already exists.");
        }
        owner.setUsername(username);
        owner.setPassword(password);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        Owner updatedOwner = ownerRepository.save(owner);
        return updatedOwner;
    }
}
