package com.example.museum.dto;

import com.example.museum.model.Donation;

public class DonationResponseDto {

    private int donationID;

    public DonationResponseDto(Donation donation){
        this.donationID = donation.getDonationID();
    }

    public int getDonationID(){
        return this.donationID;
    }
}
