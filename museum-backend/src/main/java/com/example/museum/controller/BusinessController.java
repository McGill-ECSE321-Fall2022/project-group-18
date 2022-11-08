package com.example.museum.controller;

import com.example.museum.dto.BusinessDto;
import com.example.museum.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @GetMapping("/business")
    public ResponseEntity<BusinessDto> createBusiness(@RequestBody BusinessDto business){
        BusinessDto createdBusiness = businessService.createBusiness(business);
        return new ResponseEntity<BusinessDto>(createdBusiness, HttpStatus.CREATED);
    }
}
