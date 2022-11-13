package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.BusinessHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BusinessHourService {

    @Autowired
    BusinessHourRepository businessHourRepository;

    @Transactional
    public BusinessHour createBusinessHour(BusinessHour businessHour) {
        if (businessHour.getBusinessHourID() > 0) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Request should not contain an id field");
        }
        // List<BusinessHour> bhours = (List<BusinessHour>)
        // businessHourRepository.findAll().iterator();
        Iterator<BusinessHour> bHours = businessHourRepository.findAll().iterator();
        while (bHours.hasNext()) {
            BusinessHour bh = bHours.next();
            if (bh.getDay().toString().equals(businessHour.getDay().toString())) {
                throw new DatabaseException(HttpStatus.CONFLICT, "A BusinessHour with the given date already exists");
            }
        }

        businessHour = businessHourRepository.save(businessHour);
        return businessHour;
    }

    @Transactional
    public BusinessHour getBusinessHourById(int id) {
        BusinessHour businessHour = businessHourRepository.findBusinessHourByBusinessHourID(id);
        if (businessHour == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "BusinessHour not found");
        }
        return businessHour;
    }

    @Transactional
    public List<BusinessHour> getAllBusinessHours() {
        List<BusinessHour> businessHours = new ArrayList<>();
        Iterator<BusinessHour> bHours = businessHourRepository.findAll().iterator();
        while (bHours.hasNext()) {
            BusinessHour bh = bHours.next();
            businessHours.add(bh);
        }
        return businessHours;
    }

    // TODO: do we need to save the business before returning it (to update the
    // database)?
    public BusinessHour modifyBusinessHourById(int id, Date day, Time openTime, Time closeTime) {
        Iterator<BusinessHour> bHours = businessHourRepository.findAll().iterator();
        while (bHours.hasNext()) {
            BusinessHour bh = bHours.next();
            if (bh.getDay().toString().equals(day.toString())) {
                throw new DatabaseException(HttpStatus.CONFLICT, "A BusinessHour with the given date already exists");
            }
        }
        BusinessHour businessHour = businessHourRepository.findBusinessHourByBusinessHourID(id);
        businessHour.setDay(day);
        businessHour.setOpenTime(openTime);
        businessHour.setCloseTime(closeTime);
        // I assume we need to save the new one to the database
        BusinessHour updatedBusinessHour = businessHourRepository.save(businessHour);
        return updatedBusinessHour;
    }
}
