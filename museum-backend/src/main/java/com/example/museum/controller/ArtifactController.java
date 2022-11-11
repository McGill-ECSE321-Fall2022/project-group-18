package com.example.museum.controller;


import com.example.museum.dto.ArtifactDto;
import com.example.museum.model.Artifact;
import com.example.museum.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ArtifactController {

    @Autowired
    ArtifactService artifactService;

    @GetMapping("/artifact/{artID}")
    public ResponseEntity<ArtifactDto> getArtifactByArtID(@PathVariable int artID){ //NAME CORRECT? NO CONFLICT WITH REPO FCT?
        Artifact artifact = artifactService.getArtifactByArtID(artID);
        return new ResponseEntity<ArtifactDto>(new ArtifactDto(artifact), HttpStatus.OK);
    }
    @PostMapping("/artifact")
    public ResponseEntity<ArtifactDto> createArtifact(@RequestBody ArtifactDto request){
        Artifact artifactToCreate = request.toModel();
        Artifact createdArtifact = artifactService.createArtifact(artifactToCreate);
        ArtifactDto response = new ArtifactDto(createdArtifact);
        return new ResponseEntity<ArtifactDto>(response, HttpStatus.CREATED);
    }
    //Set loan price
    // TODO Test
    @PostMapping("/artifact/update/{artID}")
    public ResponseEntity<ArtifactDto> updateArtifact(@PathVariable int artID, @RequestBody ArtifactDto request){
        Artifact updateArtifact = artifactService.modifyLoanFee(artID, request.getLoanable(), request.getLoaned(), request.getLoanFee());
        ArtifactDto response = new ArtifactDto(updateArtifact);
        return new ResponseEntity<ArtifactDto>(response, HttpStatus.OK);
    }


}

