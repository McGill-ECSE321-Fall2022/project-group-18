package com.example.museum.controller;

import com.example.museum.dto.EmployeeDto;
import com.example.museum.dto.EmployeeHourDto;
import com.example.museum.model.Employee;
import com.example.museum.service.*;

import java.util.List;

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

    @PostMapping("/employee/login")
    public ResponseEntity<String> loginEmployee(@RequestBody EmployeeDto request) {
        Employee employeeToLogin = request.toModel();
        employeeService.loginEmployee(employeeToLogin);
        return new ResponseEntity<String>("Successful login", HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeByAccountID(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeByID(id);
        return new ResponseEntity<EmployeeDto>(new EmployeeDto(employee), HttpStatus.OK);
    }
    
    @GetMapping("/employee/{id}/employeeHours")
    public ResponseEntity<List<EmployeeHourDto>> getEmployeeEmployeeHours(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeByID(id);
        EmployeeDto employeeDto = new EmployeeDto(employee);

        List<EmployeeHourDto> employeeHourDtos = employeeDto.getEmployeeHours();

        return new ResponseEntity<List<EmployeeHourDto>>(employeeHourDtos, HttpStatus.OK);
    }

    @PostMapping("/employee/{id}/update")
    public ResponseEntity<EmployeeDto> updatedEmployee(@PathVariable int id, @RequestBody EmployeeDto request) {
        Employee updatedEmployee = employeeService.modifyEmployeeByID(id, request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName());
        EmployeeDto response = new EmployeeDto(updatedEmployee);
        return new ResponseEntity<EmployeeDto>(response, HttpStatus.OK);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployeeByID(id);
        return new ResponseEntity<String>("Employee deleted successfully.", HttpStatus.OK);
    }
}
