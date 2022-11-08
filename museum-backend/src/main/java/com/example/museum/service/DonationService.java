package com.example.museum.service;

import com.example.museum.model.Donation;
import com.example.museum.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {

    @Autowired
    DonationRepository donationRepo;

    @Transactional
    public Donation getDonationByDonationID(int donationID){
        Donation donation = donationRepo.findDonationByDonationID(donationID);
        //error
        return donation;
    }

    @Transactional
    public Donation createDonation(Donation donation) {
        //error
        donation = donationRepo.save(donation);
        return donation;
    }
}
