package com.example.museum.dto;

import com.example.museum.model.Donation;

public class DonationDto {
    private int donationID;

    public DonationDto(Donation donation){
        this.donationID = donation.getDonationID();
    }

    public DonationDto(){}

    public int getDonationID(){
        return donationID;
    }

    public Donation toModel(){
        Donation donation = new Donation();
        donation.setDonationID(this.getDonationID());
        return donation;
    }
}
