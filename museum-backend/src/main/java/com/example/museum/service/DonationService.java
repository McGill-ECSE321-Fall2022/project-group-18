package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DonationService {
//
    @Autowired
    DonationRepository donationRepo;

    @Autowired
    ArtifactRepository artifactRepo;

    @Transactional
    public Donation getDonationByDonationID(int donationID){
        Donation donation = donationRepo.findDonationByDonationID(donationID);
        if(donation == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Donation not found");
        }
        return donation;
    }

    @Transactional
    public Donation createDonation(List<Artifact> artifactList) {

        Donation donation = new Donation();
        donation.setNewDonationArtifactsList();
        if(artifactList.size() == 0){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "A donation can't have no artifacts");
        }
        for(Artifact artifact: artifactList){
            donation.addDonatedArtifact(artifact);
        }

        donation = donationRepo.save(donation);
        return donation;

    }

    @Transactional
    public List<Artifact> getArtifactsInDonation(int id){
        List<Artifact> artifacts = new ArrayList<>();
        Donation donation = getDonationByDonationID(id);
        List<Artifact> arts = donation.getDonatedArtifacts();

        for (Artifact a : arts){
            artifacts.add(a);
        }
        return artifacts;
    }

}
