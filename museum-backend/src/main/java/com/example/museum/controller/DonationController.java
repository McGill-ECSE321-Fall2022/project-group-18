package com.example.museum.controller;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.dto.DonationDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import com.example.museum.service.ArtifactService;
import com.example.museum.service.DonationService;
import com.example.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DonationController {

    @Autowired
    DonationService donationService;

    @Autowired
    ArtifactService artifactService;

    @Autowired
    RoomService roomService;

    //Get donation from donationID
    @GetMapping("/donation/{donationID}")
    public ResponseEntity<DonationDto> getDonationByDonationID(@PathVariable int donationID){
        Donation donation = donationService.getDonationByDonationID(donationID);
        return new ResponseEntity<DonationDto>(new DonationDto(donation), HttpStatus.OK);
    }

    //Create a donation from a aritfact dto list
    @PostMapping("/donation")
    public ResponseEntity<DonationDto> createDonation(@RequestBody List<ArtifactDto> artifactDtoList){
        List<Artifact> artifactList = new ArrayList<>();
        for (ArtifactDto artifactDto: artifactDtoList) {
            Artifact art = artifactService.createArtifact(artifactDto.toModel());
            artifactList.add(art);
        }
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: artifactList) {
            artifactIDList.add(artifact.getArtID());
        }
        roomService.addArtifactsToRoom("Storage", artifactIDList); // add artifacts created into storage room
        Donation createdDonation = donationService.createDonation(artifactList);
        DonationDto response = new DonationDto(createdDonation);
        return new ResponseEntity<DonationDto>(response, HttpStatus.CREATED);
        }


    //Get all artifacts in donation
    @GetMapping("/donation/artifacts/{id}")
    public ResponseEntity<List<ArtifactDto>> getAllArtifactsDonation(@PathVariable int id) {
        List<Artifact> artifacts = donationService.getArtifactsInDonation(id);
        List<ArtifactDto> artifactDtoList = new ArrayList<>();
        for (Artifact a : artifacts) {
            ArtifactDto aDto = new ArtifactDto(a);
            artifactDtoList.add(aDto);
        }
        return new ResponseEntity<List<ArtifactDto>>(artifactDtoList, HttpStatus.OK);
    }
}
