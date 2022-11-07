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
    public ResponseEntity<ArtifactDto> createArtifact(@RequestBody Artifact artifact){
        artifact = artifactService.createArtifact(artifact);
        return new ResponseEntity<ArtifactDto>(new ArtifactDto(artifact), HttpStatus.CREATED);
    }
}
