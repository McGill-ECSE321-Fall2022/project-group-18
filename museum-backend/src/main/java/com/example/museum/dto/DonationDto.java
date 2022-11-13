package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;

import java.util.ArrayList;
import java.util.List;

public class DonationDto {
    private int donationID;
    private List<Integer> artifactIDList;

    public DonationDto(Donation donation){
        this.donationID = donation.getDonationID();
        artifactIDList = new ArrayList<>();
        for(Artifact a : donation.getDonatedArtifacts()){
            this.artifactIDList.add(a.getArtID());
        }
    }
    //

    public DonationDto(){}

    public int getDonationID(){
        return donationID;
    }

    public List<Integer> getArtifactList(){
        return this.artifactIDList;
    }

    public Donation toModel(){
        Donation donation = new Donation();
        donation.setDonationID(this.getDonationID());
        donation.setNewDonationArtifactsList();
        return donation;
    }
}
