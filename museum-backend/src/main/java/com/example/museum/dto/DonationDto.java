package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.BusinessHour;
import com.example.museum.model.Donation;

import java.util.ArrayList;
import java.util.List;

public class DonationDto {
    private int donationID;
    private List<ArtifactDto> artifactList;

    public DonationDto(Donation donation){
        this.donationID = donation.getDonationID();
        artifactList = new ArrayList<>();
        for(Artifact a : donation.getDonatedArtifacts()){
            this.artifactList.add(new ArtifactDto(a));
        }
    }

    public DonationDto(){}

    public int getDonationID(){
        return donationID;
    }

    public List<ArtifactDto> getArtifactList(){
        return this.artifactList;
    }

    public Donation toModel(){
        Donation donation = new Donation();
        donation.setDonationID(this.getDonationID());
        for(ArtifactDto art : this.getArtifactList()){
            donation.addDonatedArtifact(art.toModel());
        }
        return donation;
    }
}
