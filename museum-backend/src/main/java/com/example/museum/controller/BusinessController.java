package com.example.museum.controller;

import com.example.museum.dto.BusinessDto;
import com.example.museum.model.Business;
import com.example.museum.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class BusinessController {

    @Autowired
    BusinessService businessService;

    //create a business based on a business dto
    @PostMapping("/business")
    public ResponseEntity<BusinessDto> createBusiness(@RequestBody BusinessDto request) {
        Business businessToCreate = request.toModel();
        Business createdBusiness = businessService.createBusiness(businessToCreate);
        BusinessDto response = new BusinessDto(createdBusiness);
        return new ResponseEntity<BusinessDto>(response, HttpStatus.CREATED);
    }

    //get a business dto by inputting the id of the business
    @GetMapping("/business/{id}")
    public ResponseEntity<BusinessDto> getBusinessByBusinessID(@PathVariable int id) {
        Business business = businessService.getBusinessByID(id);
        return new ResponseEntity<BusinessDto>(new BusinessDto(business), HttpStatus.OK);
    }

    // update a business by inputting the id of the business and a new business dto
    @PostMapping("/business/update/{id}")
    public ResponseEntity<BusinessDto> updateBusiness(@PathVariable int id, @RequestBody BusinessDto request) {
        Business updatedBusiness = businessService.modifyBusinessById(id, request.getTicketFee(),
                request.getBusinessHours());
        BusinessDto response = new BusinessDto(updatedBusiness);
        return new ResponseEntity<BusinessDto>(response, HttpStatus.OK);
    }
}
