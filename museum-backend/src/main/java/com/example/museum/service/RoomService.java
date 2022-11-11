package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Room;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

public class RoomService {

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    ArtifactRepository artifactRepo;

    @Transactional
    public Room getRoomByID(int id) {
        if (!roomRepo.existsById(id)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Room not found");
        }
        Room room = roomRepo.findRoomByRoomID(id);
        return room;
    }

    @Transactional
    public Room createRoom(Room room) {
        if (roomRepo.existsById(room.getRoomID())) {
            throw new DatabaseException(HttpStatus.CONFLICT, "This room already exists");
        }
        room = roomRepo.save(room);
        return room;
    }

}
