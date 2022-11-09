package com.example.museum.controller;

import com.example.museum.dto.ArtifactResponseDto;
import com.example.museum.dto.BusinessDto;
import com.example.museum.model.Business;
import com.example.museum.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @PostMapping("/business")
    public ResponseEntity<BusinessDto> createBusiness(@RequestBody BusinessDto request){
        Business businessToCreate = request.toModel();
        Business createdBusiness = businessService.createBusiness(businessToCreate);
        BusinessDto response = new BusinessDto(createdBusiness);
        return new ResponseEntity<BusinessDto>(response, HttpStatus.CREATED);
    }

    @GetMapping("/business/{id}")
    public ResponseEntity<BusinessDto> getBusinessByBusinessID(@PathVariable int id){
        Business business = businessService.getBusinessByID(id);
        return new ResponseEntity<BusinessDto>(new BusinessDto(business), HttpStatus.OK);
    }
}
