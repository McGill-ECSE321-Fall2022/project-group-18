package com.example.museum.controller;

import com.example.museum.dto.DonationDto;
import com.example.museum.model.Donation;
import com.example.museum.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DonationController {
//
    @Autowired
    DonationService donationService;

    @GetMapping("/donation/{donationID}")
    public ResponseEntity<DonationDto> getDonationByDonationID(@PathVariable int donationID){
        Donation donation = donationService.getDonationByDonationID(donationID);
        return new ResponseEntity<DonationDto>(new DonationDto(donation), HttpStatus.OK);
    }

    @PostMapping("/donation")
    public ResponseEntity<DonationDto> createDonation(@RequestBody DonationDto request){
        Donation donationToCreate = request.toModel();
        Donation createdDonation = donationService.createDonation(donationToCreate);
        DonationDto response = new DonationDto(createdDonation);
        return new ResponseEntity<DonationDto>(response, HttpStatus.CREATED);

    }
}
