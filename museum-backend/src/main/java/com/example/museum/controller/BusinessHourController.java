package com.example.museum.controller;

import com.example.museum.dto.BusinessDto;
import com.example.museum.dto.BusinessHourDto;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.model.Customer;
import com.example.museum.service.BusinessHourService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businessHour")
public class BusinessHourController {

    @Autowired
    BusinessHourService businessHourService;

        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity createBusinessHour(@RequestBody BusinessHour businessHour) {
            businessHour = businessHourService.createBusinessHour(businessHour);
            return new ResponseEntity<BusinessHourDto>(new BusinessHourDto(businessHour), HttpStatus.OK);
        }

        @GetMapping("/id")
        public ResponseEntity<BusinessHourDto> getEventByName(@PathVariable int id) {
        BusinessHour businessHour = businessHourService.getBusinessHourById(id);
        return new ResponseEntity<BusinessHourDto>(new BusinessHourDto(businessHour), HttpStatus.OK);
    }




//    @GetMapping("/event/{name}")
//    public ResponseEntity<EventDto> getEventByName(@PathVariable String name) {
//        Event event = eventService.getEventByName(name);
//        return new ResponseEntity<EventDto>(new EventDto(event), HttpStatus.OK);
//    }
//
//    @PostMapping("/event")
//    public ResponseEntity<EventDto> createEvent(@RequestBody Event event) {
//        event = eventService.createEvent(event);
//        return new ResponseEntity<EventDto>(new EventDto(event), HttpStatus.CREATED);
//    }
}
