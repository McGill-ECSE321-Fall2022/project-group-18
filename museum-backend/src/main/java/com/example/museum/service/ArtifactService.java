package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

//        checkNameConflict(artifact.getArtID(), artifact.getName());
        artifact = artifactRepo.save(artifact);
        return artifact;
    }

    @Transactional
    public Artifact updateArtifact(int artID, boolean loanable, boolean loaned, int loanFee){

        Artifact artifact = artifactRepo.findByArtID(artID);
        if(artifact == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact not found");
        }


        artifact.setLoanable(loanable);
        artifact.setLoaned(loaned);
        artifact.setLoanFee(loanFee);
        Artifact updatedArtifact = artifactRepo.save(artifact);
        return  updatedArtifact;
    }

    @Transactional
    public List<Artifact> getAllArtifacts() {
        List<Artifact> artifacts = new ArrayList<>();
        Iterator<Artifact> art = artifactRepo.findAll().iterator();
        while (art.hasNext()) {
            Artifact a = art.next();
            artifacts.add(a);
        }
        return artifacts;
    }


//    private void checkNameConflict(int id, String name) throws DatabaseException{
//        Iterator<Artifact> arts = artifactRepo.findAll().iterator();
//        while (arts.hasNext()){
//            Artifact a = arts.next();
//            if(a.getName() == name && a.getArtID() != id || id == 0){
//                throw new DatabaseException(HttpStatus.CONFLICT, "An artifact with the same name already exists");
//            }
//        }
//    }


}

