package com.example.museum.repository;

import com.example.museum.model.BusinessHour;
import org.springframework.data.repository.CrudRepository;

public interface BusinessHourRepository extends CrudRepository<BusinessHour, Integer> {
    public BusinessHour findBusinessHourByBusinessHourID(int id);
}