package com.example.museum.controller;

import com.example.museum.dto.EmployeeDto;
import com.example.museum.model.Employee;
import com.example.museum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto request) {
        Employee employeeToCreate = request.toModel();
        Employee createdEmployee = employeeService.createEmployee(employeeToCreate);
        EmployeeDto response = new EmployeeDto(createdEmployee);
        return new ResponseEntity<EmployeeDto>(response, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeByBusinessID(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeByID(id);
        return new ResponseEntity<EmployeeDto>(new EmployeeDto(employee), HttpStatus.OK);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<EmployeeDto> updatedEmployee(@PathVariable int id, @RequestBody EmployeeDto request) {
        Employee updatedEmployee = employeeService.modifyEmployeeByID(id, request.getUsername(), request.getPassword());
        EmployeeDto response = new EmployeeDto(updatedEmployee);
        return new ResponseEntity<EmployeeDto>(response, HttpStatus.OK);
    }
}
