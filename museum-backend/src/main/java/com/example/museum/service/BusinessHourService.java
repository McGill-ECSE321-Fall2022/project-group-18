package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.BusinessHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BusinessHourService {

    @Autowired
    BusinessHourRepository businessHourRepository;

    @Transactional
    public BusinessHour createBusinessHour(BusinessHour businessHour) {
        if (businessHourRepository.findBusinessHourByBusinessHourID(businessHour.getBusinessHourID()) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "A business hour with the given id already exists.");
        }

        businessHour = businessHourRepository.save(businessHour);
        return businessHour;
    }

    @Transactional
    public BusinessHour getBusinessHourById(int id){
        BusinessHour businessHour = businessHourRepository.findBusinessHourByBusinessHourID(id);
        if(businessHour == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "BusinessHour not found");
        }
        return businessHour;
    }
}