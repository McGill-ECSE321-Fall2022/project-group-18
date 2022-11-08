package com.example.museum.controller;


import com.example.museum.dto.ArtifactRequestDto;
import com.example.museum.dto.ArtifactResponseDto;
import com.example.museum.model.Artifact;
import com.example.museum.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class ArtifactController {

    @Autowired
    ArtifactService artifactService;

    @GetMapping("/artifact/{artID}")
    public ResponseEntity<ArtifactResponseDto> getArtifactByArtID(@PathVariable int artID){ //NAME CORRECT? NO CONFLICT WITH REPO FCT?
        Artifact artifact = artifactService.getArtifactByArtID(artID);
        return new ResponseEntity<ArtifactResponseDto>(new ArtifactResponseDto(artifact), HttpStatus.OK);
    }
    @PostMapping("/artifact")
    public ResponseEntity<ArtifactResponseDto> createArtifact(@RequestBody ArtifactRequestDto request){
        Artifact artifactToCreate = request.toModel();
        Artifact createdArtifact = artifactService.createArtifact(artifactToCreate);
        ArtifactResponseDto response = new ArtifactResponseDto(createdArtifact);
        return new ResponseEntity<ArtifactResponseDto>(response, HttpStatus.CREATED);
    }
}
