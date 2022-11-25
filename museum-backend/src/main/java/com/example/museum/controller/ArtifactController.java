package com.example.museum.controller;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.model.Artifact;
import com.example.museum.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class ArtifactController {

    @Autowired
    ArtifactService artifactService;

    // Method to get artifact by id
    @GetMapping("/artifact/{artID}")
    public ResponseEntity<ArtifactDto> getArtifactByArtID(@PathVariable int artID) { // NAME CORRECT? NO CONFLICT WITH
                                                                                     // REPO FCT?
        Artifact artifact = artifactService.getArtifactByArtID(artID);
        return new ResponseEntity<ArtifactDto>(new ArtifactDto(artifact), HttpStatus.OK);
    }

    // Method to create artifact by using serivce and dto
    @PostMapping("/artifact")
    public ResponseEntity<ArtifactDto> createArtifact(@RequestBody ArtifactDto request) {
        Artifact artifactToCreate = request.toModel();
        Artifact createdArtifact = artifactService.createArtifact(artifactToCreate);
        ArtifactDto response = new ArtifactDto(createdArtifact);
        return new ResponseEntity<ArtifactDto>(response, HttpStatus.CREATED);
    }

    // Update artifact in function of loanable,loaned, loanfee
    @PostMapping("/artifact/update/{artID}")
    public ResponseEntity<ArtifactDto> updateArtifact(@PathVariable int artID, @RequestBody ArtifactDto request) {
        Artifact updateArtifact = artifactService.updateArtifact(artID, request.getLoanable(), request.getLoaned(),
                request.getLoanFee());
        ArtifactDto response = new ArtifactDto(updateArtifact);
        return new ResponseEntity<ArtifactDto>(response, HttpStatus.OK);
    }

    // Method to get all artifacts.
    @GetMapping("/artifact/all")
    public ResponseEntity<List<ArtifactDto>> getAllArtifacts() {
        List<Artifact> artifacts = artifactService.getAllArtifacts();
        List<ArtifactDto> artifactDtoList = new ArrayList<>();
        for (Artifact a : artifacts) {
            ArtifactDto aDto = new ArtifactDto(a);
            artifactDtoList.add(aDto);
        }
        return new ResponseEntity<List<ArtifactDto>>(artifactDtoList, HttpStatus.OK);

    }

    // Method to get all loanable artifacts.
    @GetMapping("/loanableArtifact/all")
    public ResponseEntity<List<ArtifactDto>> getAllLoanableArtifacts() {
        List<Artifact> artifacts = artifactService.getAllArtifacts();
        List<ArtifactDto> artifactDtoList = new ArrayList<>();
        for (Artifact a : artifacts) {
            if (a.getLoanable()) {
                ArtifactDto aDto = new ArtifactDto(a);
                artifactDtoList.add(aDto);
            }
        }
        return new ResponseEntity<List<ArtifactDto>>(artifactDtoList, HttpStatus.OK);
    }

}
