package com.example.museum.service;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BusinessService {
    @Autowired
    private BusinessHourRepository businessHourRepository;
    @Autowired
    private BusinessRepository businessRepository;

    @Transactional
    public Business getBusinessByID(int id) {
        Business business = businessRepository.findBusinessByBusinessID(id);
        if (business == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Business not found");
        }
        return business;
    }

    @Transactional
    public Business createBusiness(Business businessRequest) {
//        if (businessRequest.getBusinessID() > 0) {
//            throw new RequestException(HttpStatus.BAD_REQUEST, "Request should not contain an id field");
//        }
        if (businessRequest.getTicketFee() < 0) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Ticket fee should be positive");
        }
        Business business = businessRepository.save(businessRequest);
        return business;
    }

    //adds the businessHours to the current list of business hours (never removes old business hours)
    public Business modifyBusinessById(int id, int ticketFee, List<BusinessHourDto> businessHours){
        Business business = businessRepository.findBusinessByBusinessID(id);
        if (business == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Business not found");
        }
        business.setTicketFee(ticketFee);
        for(BusinessHourDto bh: businessHours){
            business.addBusinessHour(bh.toModel());
        }
        Business updatedBusiness = businessRepository.save(business);
        return updatedBusiness;
    }

    // sets business hours from businessDto to business.
    // returns true if the list is non empty, false if it's empty
//    public boolean setBusinessHours(Business business, BusinessDto businessDto) {
//        if (businessDto.getBusinessHours().isEmpty()) {
//            return false;
//        }
//        List<BusinessHourDto> businessHours = businessDto.getBusinessHours();
//        for (BusinessHourDto bh : businessHours) {
//            int id = bh.getBusinessHourID();
//            Date day = bh.getDay();
//            Time openTime = bh.getOpenTime();
//            Time closeTime = bh.getCloseTime();
//            business.addBusinessHour(new BusinessHour(id, day, openTime, closeTime));
//        }
//        return true;
//    }
}
