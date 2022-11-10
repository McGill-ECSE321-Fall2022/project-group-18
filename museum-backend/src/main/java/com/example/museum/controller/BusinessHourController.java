package com.example.museum.controller;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.service.BusinessHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/businessHour")
public class BusinessHourController {

    @Autowired
    BusinessHourService businessHourService;

    //tested
    @PostMapping("/businessHour")
    public ResponseEntity<BusinessHourDto> createBusinessHour(@RequestBody BusinessHourDto request) {
        BusinessHour businessHourToCreate = request.toModel();
        BusinessHour createdBusinessHour = businessHourService.createBusinessHour(businessHourToCreate);
        BusinessHourDto response = new BusinessHourDto(createdBusinessHour);
        return new ResponseEntity<BusinessHourDto>(response, HttpStatus.CREATED);
    }

    /*
    TODO: test!
    - create a business hour in the database
    - set new business hour fields inside a new BusinessHour object and call this method (with the id of the first business hour)
    - make sure the id didn't change?
    - make sure the fields from the database object were modified to the fields from the second business hour object
     */
    @PostMapping("/businessHour/update/{id}")
    public ResponseEntity<BusinessHourDto> updateBusinessHour(@RequestBody BusinessHourDto request, @PathVariable int id){
        BusinessHour updatedBusinessHour = businessHourService.modifyBusinessHourById(id, request.getDay(), request.getOpenTime(), request.getCloseTime());
        BusinessHourDto response = new BusinessHourDto(updatedBusinessHour);
        return new ResponseEntity<BusinessHourDto>(response, HttpStatus.OK);
    }

    //tested
    @GetMapping("/businessHour/{id}")
    public ResponseEntity<BusinessHourDto> getEventByName(@PathVariable int id) {
        BusinessHour businessHour = businessHourService.getBusinessHourById(id);
        return new ResponseEntity<BusinessHourDto>(new BusinessHourDto(businessHour), HttpStatus.OK);
    }

    /*
    TODO: More functionality:
    - delete business hour?
     */


}
