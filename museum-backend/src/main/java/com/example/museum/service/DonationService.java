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
        for(Artifact artifact: artifactList){
            if (artifactRepo.existsById(artifact.getArtID())) {
                throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact for Donation is already in database");
            }
            artifact = artifactRepo.save(artifact);
            donation.addDonatedArtifact(artifact);
        }
        donation = donationRepo.save(donation);
        return donation;

    }
}
