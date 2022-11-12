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
// @RequestMapping("/businessHour")
public class BusinessHourController {

    @Autowired
    BusinessHourService businessHourService;

    // tested
    @PostMapping("/businessHour")
    public ResponseEntity<BusinessHourDto> createBusinessHour(@RequestBody BusinessHourDto request) {
        BusinessHour businessHourToCreate = request.toModel();
        BusinessHour createdBusinessHour = businessHourService.createBusinessHour(businessHourToCreate);
        BusinessHourDto response = new BusinessHourDto(createdBusinessHour);
        return new ResponseEntity<BusinessHourDto>(response, HttpStatus.CREATED);
    }

    // tested
    @PutMapping("/businessHour/{id}")
    public ResponseEntity<BusinessHourDto> updateBusinessHour(@PathVariable int id,
            @RequestBody BusinessHourDto request) {
        BusinessHour updatedBusinessHour = businessHourService.modifyBusinessHourById(id, request.getDay(),
                request.getOpenTime(), request.getCloseTime());
        BusinessHourDto response = new BusinessHourDto(updatedBusinessHour);
        return new ResponseEntity<BusinessHourDto>(response, HttpStatus.OK);
    }

    // tested
    @GetMapping("/businessHour/{id}")
    public ResponseEntity<BusinessHourDto> getEventByName(@PathVariable int id) {
        BusinessHour businessHour = businessHourService.getBusinessHourById(id);
        return new ResponseEntity<BusinessHourDto>(new BusinessHourDto(businessHour), HttpStatus.OK);
    }

    /*
     * TODO: More functionality:
     * - delete business hour?
     */

}
