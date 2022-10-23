package com.example.museum.repository;

import com.example.museum.model.Donation;
import org.springframework.data.repository.CrudRepository;

public interface DonationRepository extends CrudRepository<Donation, Integer> {
    public Donation findDonationByDonationID(int id);
}
