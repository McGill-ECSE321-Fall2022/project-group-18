package com.example.museum.controller;

import com.example.museum.dto.EmployeeHourDto;
import com.example.museum.model.EmployeeHour;
import com.example.museum.service.EmployeeHourService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController

public class EmployeeHourController {

    @Autowired
    EmployeeHourService employeeHourService;

    // create an employee hour by passing an employee hour dto
    @PostMapping("/employeeHour")
    public ResponseEntity<EmployeeHourDto> createEmployeeHour(@RequestBody EmployeeHourDto request) {
        EmployeeHour employeeHourToCreate = request.toModel();
        EmployeeHour createdEmployeeHour = employeeHourService.createEmployeeHour(employeeHourToCreate);
        EmployeeHourDto response = new EmployeeHourDto(createdEmployeeHour);
        return new ResponseEntity<EmployeeHourDto>(response, HttpStatus.CREATED);
    }

    // update an employee hour by passing the id of the hour to update and a dto for the updated employee hour
    @PostMapping("/employeeHour/update/{id}")
    public ResponseEntity<EmployeeHourDto> updateEmployeeHour(@PathVariable int id,
            @RequestBody EmployeeHourDto request) {
        EmployeeHour updateEmployeeHour = employeeHourService.modifyEmployeeHourById(id, request.getDay(),
                request.getStartTime(), request.getEndTime());
        EmployeeHourDto response = new EmployeeHourDto(updateEmployeeHour);
        return new ResponseEntity<EmployeeHourDto>(response, HttpStatus.OK);
    }

    //get an employee hour dto by id
    @GetMapping("/employeeHour/{id}")
    public ResponseEntity<EmployeeHourDto> getEventByName(@PathVariable int id) {
        EmployeeHour employeeHour = employeeHourService.getEmployeeHourById(id);
        return new ResponseEntity<EmployeeHourDto>(new EmployeeHourDto(employeeHour), HttpStatus.OK);
    }

    //get a list of all employee hours (as a list of dtos)
    @GetMapping("/employeeHour/all")
    public ResponseEntity<List<EmployeeHourDto>> getAllEmployeeHours() {
        List<EmployeeHour> employeeHours = employeeHourService.getAllEmployeeHours();
        List<EmployeeHourDto> employeeHourDtoList = new ArrayList<>();
        for (EmployeeHour eh : employeeHours) {
            EmployeeHourDto ehDto = new EmployeeHourDto(eh);
            employeeHourDtoList.add(ehDto);
        }
        return new ResponseEntity<List<EmployeeHourDto>>(employeeHourDtoList, HttpStatus.OK);
    }

}