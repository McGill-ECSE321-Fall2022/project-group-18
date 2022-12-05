package com.example.museum.controller;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.model.BusinessHour;
import com.example.museum.service.BusinessHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class BusinessHourController {

    @Autowired
    BusinessHourService businessHourService;

    // create a business hour with a business dto
    @PostMapping("/businessHour")
    public ResponseEntity<BusinessHourDto> createBusinessHour(@RequestBody BusinessHourDto request) {
        BusinessHour businessHourToCreate = request.toModel();
        BusinessHour createdBusinessHour = businessHourService.createBusinessHour(businessHourToCreate);
        BusinessHourDto response = new BusinessHourDto(createdBusinessHour);
        return new ResponseEntity<BusinessHourDto>(response, HttpStatus.CREATED);
    }

    // update a business hour by id and input the updated business hour as a dto
    @PostMapping("/businessHour/update/{id}")
    public ResponseEntity<BusinessHourDto> updateBusinessHour(@PathVariable int id,
            @RequestBody BusinessHourDto request) {
        BusinessHour updatedBusinessHour = businessHourService.modifyBusinessHourById(id, request.getDay(),
                request.getOpenTime(), request.getCloseTime());
        BusinessHourDto response = new BusinessHourDto(updatedBusinessHour);
        return new ResponseEntity<BusinessHourDto>(response, HttpStatus.OK);
    }

    // get a business hour by id (returns a business hour dto)
    @GetMapping("/businessHour/{id}")
    public ResponseEntity<BusinessHourDto> getBusinessHourByBusinessHourID(@PathVariable int id) {
        BusinessHour businessHour = businessHourService.getBusinessHourById(id);
        return new ResponseEntity<BusinessHourDto>(new BusinessHourDto(businessHour), HttpStatus.OK);
    }

    //get all business hours (returns a list of business hour dtos)
    @GetMapping("/businessHour/all")
    public ResponseEntity<List<BusinessHourDto>> getAllBusinessHours() {
        List<BusinessHour> businessHours = businessHourService.getAllBusinessHours();
        List<BusinessHourDto> businessHourDtoList = new ArrayList<>();
        for (BusinessHour bh : businessHours) {
            BusinessHourDto bhDto = new BusinessHourDto(bh);
            businessHourDtoList.add(bhDto);
        }
        return new ResponseEntity<List<BusinessHourDto>>(businessHourDtoList, HttpStatus.OK);
    }
}
