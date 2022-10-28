package com.example.museum.repository;

import com.example.museum.model.Business;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface BusinessRepository extends CrudRepository<Business, Integer> {
    // DAO implementation here
    // using businessID to find Business object
    public Business findBusinessByBusinessID(int id);
}
