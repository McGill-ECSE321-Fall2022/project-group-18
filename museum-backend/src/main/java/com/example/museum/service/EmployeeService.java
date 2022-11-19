package com.example.museum.service;

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
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Transactional
    public Employee getEmployeeByID(int id) {
        Employee employee = employeeRepository.findByAccountID(id);
        if (employee == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        return employee;
    }

    @Transactional
    public void loginEmployee(Employee employeeRequest) {
        Iterator<Employee> employees = employeeRepository.findAll().iterator();

        while (employees.hasNext()) {
            Employee employee = employees.next();
            if (employee.getUsername().equals(employeeRequest.getUsername())) {
                if (employee.getPassword().equals(employeeRequest.getPassword())) {
                    return;
                } else {
                    throw new RequestException(HttpStatus.BAD_REQUEST, "Wrong password");
                }
            }
        }

        throw new DatabaseException(HttpStatus.NOT_FOUND, "Employee does not exist");
    }

    @Transactional
    public Employee createEmployee(Employee employeeRequest) {
        if (employeeRepository.findByAccountID(employeeRequest.getAccountID()) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "An employee with the given id already exists.");
        }
        if (ServiceUtils.conflictingUsername(employeeRequest.getUsername(), employeeRequest.getAccountID(), customerRepository, employeeRepository,
                ownerRepository)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "An employee with the given username already exists.");
        }
        Employee employee = employeeRepository.save(employeeRequest);
        return employee;
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

    public Employee modifyEmployeeByID(int id, String username, String password, String firstName, String lastName) {
        Employee employee = employeeRepository.findByAccountID(id);
        if(employee == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        if (ServiceUtils.conflictingUsername(username, id, customerRepository, employeeRepository,
                ownerRepository)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "An employee with the given username already exists.");
        }
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        Employee updatedEmployee = employeeRepository.save(employee);
        return updatedEmployee;
    }

    @Transactional
    public boolean deleteEmployeeByID(int id) {
        Employee employee = employeeRepository.findByAccountID(id);
        if (employee == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        employeeRepository.deleteById(id);
        return true;
    }
}
