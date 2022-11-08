package com.example.museum.controller;

import com.example.museum.dto.DonationRequestDto;
import com.example.museum.dto.DonationResponseDto;
import com.example.museum.model.Donation;
import com.example.museum.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DonationController {

    @Autowired
    DonationService donationService;

    @GetMapping("/donation/{donationID}")
    public ResponseEntity<DonationResponseDto> getDonationByDonationID(@PathVariable int donationID){
        Donation donation = donationService.getDonationByDonationID(donationID);
        return new ResponseEntity<DonationResponseDto>(new DonationResponseDto(donation), HttpStatus.OK);
    }

    @PostMapping("/donation")
    public ResponseEntity<DonationResponseDto> createDonation(@RequestBody DonationRequestDto request){
        Donation donationToCreate = request.toModel();
        Donation createdDonation = donationService.createDonation(donationToCreate);
        DonationResponseDto response = new DonationResponseDto(createdDonation);
        return new ResponseEntity<DonationResponseDto>(response, HttpStatus.CREATED);

    }
}
