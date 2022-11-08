package com.example.museum.service;

import com.example.museum.dto.BusinessDto;
import com.example.museum.dto.BusinessHourDto;
import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class BusinessService {
    @Autowired
    private BusinessHourRepository businessHourRepository;
    @Autowired
    private BusinessRepository businessRepository;

    @Transactional
    public BusinessDto getBusinessByID(int id){
        Business business = businessRepository.findBusinessByBusinessID(id);
        if(business == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Business not found");
        }
        return new BusinessDto(business);
    }

    @Transactional
    public BusinessDto createBusiness(BusinessDto businessRequest){
        Business business = new Business();
        setBusinessHours(business, businessRequest);
        return new BusinessDto(business);
    }

    //sets business hours from businessDto to business.
    //returns true if the list is non empty, false if it's empty
    public boolean setBusinessHours(Business business, BusinessDto businessDto){
        if(businessDto.getBusinessHours().isEmpty()){
            return false;
        }
        List<BusinessHourDto> businessHours = businessDto.getBusinessHours();
        for(BusinessHourDto bh : businessHours){
            int id = bh.getBusinessHourID();
            Date day = bh.getDay();
            Time openTime = bh.getOpenTime();
            Time closeTime = bh.getCloseTime();
            business.addBusinessHour(new BusinessHour(id, day, openTime, closeTime));
        }
        return true;
    }
}
