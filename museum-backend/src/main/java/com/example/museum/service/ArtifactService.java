package com.example.museum.service;

import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ArtifactService {

    @Autowired
    ArtifactRepository artifactRepo;

    @Transactional
    public Artifact getArtifactByArtID(int artID){
        Artifact artifact = artifactRepo.findByArtID(artID);
        // TODO if(artifact == null)
        return artifact;
    }

    @Transactional
    public Artifact createArtifact(Artifact artifact){

        artifact = artifactRepo.save(artifact);
        //TODO if (eventRepo.findEventByName(event.getName()) != null) {
        //			throw new EventRegistrationException(HttpStatus.CONFLICT, "An event with the given name already exists.");
        //		}
        return artifact;
    }
}
