package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Donation;
import com.example.museum.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {

    @Autowired
    DonationRepository donationRepo;

    @Transactional
    public Donation getDonationByDonationID(int donationID){
        Donation donation = donationRepo.findDonationByDonationID(donationID);
        if(donation == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Donation not found");
        }
        return donation;
    }

    @Transactional
    public Donation createDonation(Donation donation) {
        if(donationRepo.findDonationByDonationID(donation.getDonationID()) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "A donation with the given id already exists.");
        }
        donation = donationRepo.save(donation);
        return donation;
    }
}
