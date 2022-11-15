package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
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
//        if (businessHour.getBusinessHourID() > 0) {
//            throw new RequestException(HttpStatus.BAD_REQUEST, "Request should not contain an id field");
//        }
        checkDateConflict(businessHour.getBusinessHourID(), businessHour.getDay());
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
        checkDateConflict(id, day);
        BusinessHour businessHour = businessHourRepository.findBusinessHourByBusinessHourID(id);
        businessHour.setDay(day);
        businessHour.setOpenTime(openTime);
        businessHour.setCloseTime(closeTime);

        BusinessHour updatedBusinessHour = businessHourRepository.save(businessHour);
        return updatedBusinessHour;
    }

    //check that the date does not already exist
    //for update, we check that the desired new date does not already exist in another BusinessHour
    //for creation, we check that the desired new date does not already exist
    private void checkDateConflict(int id, Date day) throws DatabaseException{
        Iterator<BusinessHour> bHours = businessHourRepository.findAll().iterator();
        while (bHours.hasNext()) {
            BusinessHour bh = bHours.next();
            if (bh.getDay().toString().equals(day.toString()) && (bh.getBusinessHourID() != id || id == 0)) {
                throw new DatabaseException(HttpStatus.CONFLICT, "A BusinessHour with the given date already exists");
            }
        }
    }
}
