package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;

import java.util.ArrayList;
import java.util.List;

public class DonationDto {
    private int donationID;
    private List<ArtifactDto> artifacts;

    public DonationDto(Donation donation){
        this.donationID = donation.getDonationID();
        artifacts = new ArrayList<>();
        for(Artifact a : donation.getDonatedArtifacts()){
            this.artifacts.add(new ArtifactDto(a));
        }
    }
    //

    public DonationDto(){}

    public int getDonationID(){
        return donationID;
    }

    public List<ArtifactDto> getArtifacts(){
        return artifacts;
    }

    public Donation toModel(){
        Donation donation = new Donation();
        donation.setDonationID(this.getDonationID());
        for(ArtifactDto a : this.getArtifacts()){
            Artifact art = a.toModel();
            donation.addDonatedArtifact(art);
        }
        return donation;
    }
}
