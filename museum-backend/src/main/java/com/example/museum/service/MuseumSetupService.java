package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Owner;
import com.example.museum.model.Room;
import com.example.museum.repository.OwnerRepository;
import com.example.museum.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

@Service
public class MuseumSetupService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    OwnerRepository ownerRepository;

    public List<Room> createRooms(){
        if(roomRepository.findAll().iterator().hasNext()){
            throw new DatabaseException(HttpStatus.CONFLICT, "Rooms already exist in the database");
        }
        List<Room> roomList = new ArrayList<>();

        //small rooms
        for(int i = 1; i <= 5; i ++){
            Room room = new Room();
            room.setCapacity(200);
            room.setNewRoomArtifactsList();
            room.setName("SR" + i);
            Room createdRoom = roomRepository.save(room);
            roomList.add(createdRoom);
        }

        //large rooms
        for(int i = 1; i <= 5; i++){
            Room room = new Room();
            room.setCapacity(300);
            room.setNewRoomArtifactsList();
            room.setName("LR" + i);
            Room createdRoom = roomRepository.save(room);
            roomList.add(createdRoom);
        }

        //storage
        Room storage = new Room();
        storage.setCapacity(MAX_VALUE);
        storage.setNewRoomArtifactsList();
        storage.setName("Storage");
        Room createdStorage = roomRepository.save(storage);
        roomList.add(createdStorage);

        return roomList;
    }

    public Owner createOwner(){
        Owner owner = new Owner();
        owner.setUsername("owner321");
        owner.setPassword("ecse321");
        owner.setFirstName("Marwan");
        owner.setLastName("Kanaan");

        if(ownerRepository.findAll().iterator().hasNext()){
            throw new DatabaseException(HttpStatus.CONFLICT, "Owner already exists in the database");
        }
        Owner returnedOwner = ownerRepository.save(owner);
        return returnedOwner;
    }
}
