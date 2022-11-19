package com.example.museum.controller;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Owner;
import com.example.museum.model.Room;
import com.example.museum.service.MuseumSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MuseumSetupController {
    @Autowired
    MuseumSetupService museumSetupService;

    @PostMapping("/setup")
    public ResponseEntity<Boolean> setupMuseum(){
        try{
            Owner createdOwner = museumSetupService.createOwner();
            List<Room> createdRooms = museumSetupService.createRooms();
        }catch(DatabaseException e){
            return new ResponseEntity<Boolean>(Boolean.FALSE, e.getStatus());
        }

        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.CREATED);
    }
}
