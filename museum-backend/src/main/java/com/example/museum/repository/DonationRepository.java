package com.example.museum.repository;

import com.example.museum.model.Donation;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface DonationRepository extends CrudRepository<Donation, Integer> {
    // DAO implementation here
    // using donationID to find Donation object
    public Donation findDonationByDonationID(int id);
}
