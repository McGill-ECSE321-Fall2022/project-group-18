package com.example.museum.dto;

import com.example.museum.model.Donation;

public class DonationRequestDto {
    private int donationID;

    public void setDonationID(int donationID){
        this.donationID = donationID;
    }

    public int getDonationID(){
        return this.donationID;
    }

    public Donation toModel(){
        Donation donation = new Donation();
        donation.setDonationID(this.donationID);
        return donation;
    }
}
