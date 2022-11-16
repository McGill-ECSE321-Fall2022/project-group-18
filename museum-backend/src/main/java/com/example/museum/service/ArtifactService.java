package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ArtifactService {

    @Autowired
    ArtifactRepository artifactRepo;

    @Transactional
    public Artifact getArtifactByArtID(int artID){
        Artifact artifact = artifactRepo.findByArtID(artID);
        if(artifact == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact not found");
        }
        return artifact;
    }

    @Transactional
    public Artifact createArtifact(Artifact artifact){

        if(artifactRepo.findByArtID(artifact.getArtID()) != null){
            throw new DatabaseException(HttpStatus.CONFLICT, "An artifact with the given id already exists.");
        }

        artifact = artifactRepo.save(artifact);
        return artifact;
    }

    @Transactional
    public Artifact updateArtifact(int artID, boolean loanable, boolean loaned, int loanFee){
        Artifact artifact = artifactRepo.findByArtID(artID);
        artifact.setLoanable(loanable);
        artifact.setLoaned(loaned);
        artifact.setLoanFee(loanFee);
        Artifact updatedArtifact = artifactRepo.save(artifact);
        return  updatedArtifact;
    }

    @Transactional
    public List<Artifact> getAllArtifacts() {
        List<Artifact> Artifacts = new ArrayList<>();
        Iterator<Artifact> Art = ArtifactRepository.findAll().iterator();
        while (Art.hasNext()) {
            Artifact a = Art.next();
            Artifacts.add(a);
        }
        return Artifacts;
    }


}

