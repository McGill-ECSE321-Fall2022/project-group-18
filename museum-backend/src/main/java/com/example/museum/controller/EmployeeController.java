package com.example.museum.controller;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.dto.EmployeeDto;
import com.example.museum.dto.EmployeeHourDto;
import com.example.museum.model.BusinessHour;
import com.example.museum.model.Employee;
import com.example.museum.service.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    //create an employee by passing an employee dto
    @PostMapping("/employee")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto request) {
        Employee employeeToCreate = request.toModel();
        Employee createdEmployee = employeeService.createEmployee(employeeToCreate);
        EmployeeDto response = new EmployeeDto(createdEmployee);
        return new ResponseEntity<EmployeeDto>(response, HttpStatus.CREATED);
    }

    //log in an employee by passing in the employee dto (this will check their credentials)
    @PostMapping("/employee/login")
    public ResponseEntity<Integer> loginEmployee(@RequestBody EmployeeDto request) {
        Employee employeeToLogin = request.toModel();
        int id = employeeService.loginEmployee(employeeToLogin);
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    //get the employee dto by inputting an id
    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeByAccountID(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeByID(id);
        return new ResponseEntity<EmployeeDto>(new EmployeeDto(employee), HttpStatus.OK);
    }

    //get employee hours for any given id by inputting the id of the desired employee
    @GetMapping("/employee/{id}/employeeHours")
    public ResponseEntity<List<EmployeeHourDto>> getEmployeeEmployeeHours(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeByID(id);
        EmployeeDto employeeDto = new EmployeeDto(employee);

        List<EmployeeHourDto> employeeHourDtos = employeeDto.getEmployeeHours();

        return new ResponseEntity<List<EmployeeHourDto>>(employeeHourDtos, HttpStatus.OK);
    }

    //update an employee by inputting the id of the employee to update and the new fields inside a new employee dto
    @PostMapping("/employee/update/{id}")
    public ResponseEntity<EmployeeDto> updatedEmployee(@PathVariable int id, @RequestBody EmployeeDto request) {
        Employee updatedEmployee = employeeService.modifyEmployeeByID(id, request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName(), request.toModel().getEmployeeHours());
        EmployeeDto response = new EmployeeDto(updatedEmployee);
        return new ResponseEntity<EmployeeDto>(response, HttpStatus.OK);
    }

    //delete an employee by their id
    // not best practice to make it a POST, but allows the integration tests to work
    // seemlessly
    @PostMapping("/employee/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployeeByID(id);
        return new ResponseEntity<String>("Employee deleted successfully.", HttpStatus.OK);
    }

    //get a list of all employees (returns a list of employee dtos)
    @GetMapping("/employee/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee em : employees) {
            EmployeeDto emDto = new EmployeeDto(em);
            employeeDtoList.add(emDto);
        }
        return new ResponseEntity<List<EmployeeDto>>(employeeDtoList, HttpStatus.OK);
    }
}
