package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Room;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    ArtifactRepository artifactRepo;

    // get a Room object using room id
    @Transactional
    public Room getRoomByID(int id) {
        if (!roomRepo.existsById(id)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Room not found");
        }
        Room room = roomRepo.findRoomByRoomID(id);
        return room;
    }

    // get all rooms
    @Transactional
    public List<Room> getAllRooms() {
        return (List<Room>) (ArrayList<Room>) roomRepo.findAll();
    }

    // get all rooms ID
    @Transactional
    public List<Integer> getAllRoomsID() {
        List<Room> roomList = (ArrayList<Room>) roomRepo.findAll();
        List<Integer> roomIDList = new ArrayList<>();
        for (Room room: roomList) {
            roomIDList.add(room.getRoomID());
        }
        return roomIDList;
    }

    // get Map<artifactID, roomID> for all artifacts in rooms
    @Transactional
    public Map<Integer, Integer> getAllRoomsAndArtifacts() { // key is art id, value is room id
        List<Room> roomList = this.getAllRooms();
        Map<Integer, Integer> allRoomsAndArtifacts = new HashMap<>();

        for (Room room: roomList) {
            List<Artifact> artifactsInRoom = room.getRoomArtifacts();
            for (Artifact artifact: artifactsInRoom) {
                allRoomsAndArtifacts.put(artifact.getArtID(), room.getRoomID());
            }
        }
        return allRoomsAndArtifacts;
    }


    // add an artifact from empty (i.e. from Loan or Donation)
    @Transactional
    public Room addArtifactsToRoom(int roomID, List<Integer> artifactIDList) {
        // check if the room pass in exists
        if (!roomRepo.existsById(roomID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "This room does not exist");
        }
        Room room = roomRepo.findRoomByRoomID(roomID); // room should exist
        // check if the size of the adding artifacts exceed the capacity
        int totalRoomArtifactsNum = room.getRoomArtifacts().size() + artifactIDList.size();
        if ( room.getCapacity() > 0 && totalRoomArtifactsNum > room.getCapacity()) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Display room's artifacts cannot exceed its capacity");
        }
        // add artifacts to room
        for (int artifactID: artifactIDList) {
            if (!artifactRepo.existsById(artifactID)) {
                throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact for Room is not in database");
            }
            Map<Integer, Integer> allArtifactsAndRooms = this.getAllRoomsAndArtifacts();
            if (allArtifactsAndRooms.containsKey(artifactID)) { // if this art id is already in a room
                throw new DatabaseException(HttpStatus.CONFLICT, "This artifact already exists in rooms");
            }
            Artifact artifact = artifactRepo.findByArtID(artifactID);
            room.addRoomArtifact(artifact);
        }
        room = roomRepo.save(room);
        return room;
    }

    // same to add artifacts into a room, but find the room using room name in String
    @Transactional
    public Room addArtifactsToRoom(String roomName, List<Integer> artifactIDList) {
        // check if the room pass in exists
        if (!roomRepo.existsByName(roomName)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "This room does not exist");
        }
        Room room = roomRepo.findRoomByName(roomName);
        // check if the size of the adding artifacts exceed the capacity
        int totalRoomArtifactsNum = room.getRoomArtifacts().size() + artifactIDList.size();
        if ( room.getCapacity() >= 0 && totalRoomArtifactsNum > room.getCapacity()) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Display room's artifacts cannot exceed its capacity");
        }
        // add artifacts to room
        for (int artifactID: artifactIDList) {
            if (!artifactRepo.existsById(artifactID)) {
                throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact for Room is not in database");
            }
            Map<Integer, Integer> allArtifactsAndRooms = this.getAllRoomsAndArtifacts();
            if (allArtifactsAndRooms.containsKey(artifactID)) { // if this art id is already in a room
                throw new DatabaseException(HttpStatus.CONFLICT, "This artifact already exists in rooms");
            }
            Artifact artifact = artifactRepo.findByArtID(artifactID);
            room.addRoomArtifact(artifact);
        }
        room = roomRepo.save(room);
        return room;
    }

    // transfer a single artifact from src room to dest room.
    // a possible improvement: only provide artifact id and dest room ID to complete this move
    @Transactional
    public List<Room> transferArtifactBetweenRooms(int srcRoomID, int destRoomID, int artifactID) {
        if (!roomRepo.existsById(srcRoomID) || !roomRepo.existsById(destRoomID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "The room does not exist");
        }
        Room srcRoom = roomRepo.findRoomByRoomID(srcRoomID);
        Room destRoom = roomRepo.findRoomByRoomID(destRoomID);
        // check size of new room
        int destRoomNum = destRoom.getRoomArtifacts().size() + 1;
        if (destRoom.getCapacity() >= 0 && destRoomNum > destRoom.getCapacity()) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Display room's artifacts cannot exceed its capacity");
        }
        if (!artifactRepo.existsById(artifactID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "The artifact does not exist");
        }
        Artifact artifact = artifactRepo.findByArtID(artifactID);
        Map<Integer, Integer> allArtifactsAndRooms = this.getAllRoomsAndArtifacts();
        if ( allArtifactsAndRooms.get(artifactID) == destRoomID || allArtifactsAndRooms.get(artifactID) != srcRoomID) {
            throw new DatabaseException(HttpStatus.CONFLICT, "The artifact conflicts with this transfer");
        }
        srcRoom.removeRoomArtifact(artifact);
        destRoom.addRoomArtifact(artifact);
        srcRoom = roomRepo.save(srcRoom);
        destRoom = roomRepo.save(destRoom);
        List<Room> roomList = new ArrayList<>();
        roomList.add(0, srcRoom);
        roomList.add(1, destRoom);
        return roomList;
    }

    // remove a list of artifacts from their rooms (not necessarily removed from the same room)
    @Transactional
    public List<Room> removeArtifactsFromRooms(List<Artifact> removedArtifactList) {
        // get all artifacts rooms
        List<Room> roomList = new ArrayList<>();
        for (Artifact artifact: removedArtifactList) {
            // a not loaned artifact must be in a room, display or storage
            if (!this.getAllRoomsAndArtifacts().containsKey(artifact.getArtID())) {
                throw new DatabaseException(HttpStatus.NOT_FOUND, "The artifact to remove is not found in any room");
            }
            int roomID = this.getAllRoomsAndArtifacts().get(artifact.getArtID());
            Room room = roomRepo.findRoomByRoomID(roomID);
            room.removeRoomArtifact(artifact); // remove from their room
            room = roomRepo.save(room);
            roomList.add(room);
        }
        return roomList;
    }

    // create a room with no artifact inside. specify its name and capacity
    @Transactional
    public Room createRoom(String roomName, int roomCapacity) {
        if (roomRepo.existsByName(roomName)) {
            throw new DatabaseException(HttpStatus.BAD_REQUEST, "This room name already exists");
        }
        Room room = new Room();
        room.setName(roomName);
        room.setCapacity(roomCapacity);
        room.setNewRoomArtifactsList();
        room = roomRepo.save(room);
        return room;
    }
}
