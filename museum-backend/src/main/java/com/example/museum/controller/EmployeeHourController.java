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

@RestController

public class EmployeeHourController {

    @Autowired
    EmployeeHourService employeeHourService;

    //tested
    @PostMapping("/employeeHour")
    public ResponseEntity<EmployeeHourDto> createEmployeeHour(@RequestBody EmployeeHourDto request) {
        EmployeeHour employeeHourToCreate = request.toModel();
        EmployeeHour createdEmployeeHour = employeeHourService.createEmployeeHour(employeeHourToCreate);
        EmployeeHourDto response = new EmployeeHourDto(createdEmployeeHour);
        return new ResponseEntity<EmployeeHourDto>(response, HttpStatus.CREATED);
    }

    //tested
    @PostMapping("/employeeHour/update/{id}")
    public ResponseEntity<EmployeeHourDto> updateEmployeeHour(@PathVariable int id, @RequestBody EmployeeHourDto request){
        EmployeeHour updateEmployeeHour = employeeHourService.modifyEmployeeHourById(id, request.getDay(), request.getStartTime(), request.getEndTime());
        EmployeeHourDto response = new EmployeeHourDto(updateEmployeeHour);
        return new ResponseEntity<EmployeeHourDto>(response, HttpStatus.OK);
    }

    @GetMapping("/employeeHour/{id}")
    public ResponseEntity<EmployeeHourDto> getEventByName(@PathVariable int id) {
        EmployeeHour employeeHour = employeeHourService.getEmployeeHourById(id);
        return new ResponseEntity<EmployeeHourDto>(new EmployeeHourDto(employeeHour), HttpStatus.OK);
    }

    @GetMapping("/employeeHour/all")
    public ResponseEntity<List<EmployeeHourDto>> getAllEmployeeHours(){
        List<EmployeeHour> employeeHours = employeeHourService.getAllEmployeeHours();
        List<EmployeeHourDto> employeeHourDtoList = new ArrayList<>();
        for(EmployeeHour eh : employeeHours){
            EmployeeHourDto ehDto = new EmployeeHourDto(eh);
            employeeHourDtoList.add(ehDto);
        }
        return new ResponseEntity<List<EmployeeHourDto>>(employeeHourDtoList, HttpStatus.OK);
    }

}