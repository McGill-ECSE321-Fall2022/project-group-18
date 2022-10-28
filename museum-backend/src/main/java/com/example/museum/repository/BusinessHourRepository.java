package com.example.museum.repository;

import com.example.museum.model.BusinessHour;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface BusinessHourRepository extends CrudRepository<BusinessHour, Integer> {
    // DAO implementation here
    // using businessHourID to find BusinessHour object
    BusinessHour findBusinessHourByBusinessHourID(int id);
}