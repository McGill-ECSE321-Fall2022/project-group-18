package com.example.museum.controller;

import com.example.museum.model.Employee;
import com.example.museum.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createEmployee(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Employee employee = mapper.readValue(body, Employee.class);
            Employee persistedEmployee = employeeService.createEmployee(employee);
            return new ResponseEntity<>(persistedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEmployee(@PathVariable(name = "id") int id) {
        return employeeService.retrieveEmployee(id)
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
