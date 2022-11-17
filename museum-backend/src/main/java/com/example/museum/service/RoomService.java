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

    @Transactional
    public Room getRoomByID(int id) {
        if (!roomRepo.existsById(id)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Room not found");
        }
        Room room = roomRepo.findRoomByRoomID(id);
        return room;
    }

    @Transactional
    public List<Room> getAllRooms() {
        List<Room> roomList = (ArrayList<Room>) roomRepo.findAll();
        return roomList;
    }

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

//    @Transactional
//    public Room createRoom(Room room) {
//        if (roomRepo.existsById(room.getRoomID())) {
//            throw new DatabaseException(HttpStatus.CONFLICT, "This room already exists");
//        }
//        room = roomRepo.save(room);
//        return room;
//    }

    // add an artifact from empty (i.e. from Loan or Donation)
    @Transactional
    public Room addArtifactsToRoom(int roomID, List<Integer> artifactIDList) {
        // check if the room pass in exists
        if (!roomRepo.existsById(roomID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "This room does not exist");
        }
        Room room = roomRepo.findRoomByRoomID(roomID);
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

    @Transactional
    public Room addArtifactsToRoom(String roomName, List<Integer> artifactIDList) {
        // check if the room pass in exists
        if (!roomRepo.existsByName(roomName)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "This room does not exist");
        }
        Room room = roomRepo.findRoomByName(roomName);
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

    @Transactional
    public List<Room> transferArtifactBetweenRooms(int srcRoomID, int destRoomID, int artifactID) {
        if (!roomRepo.existsById(srcRoomID) || !roomRepo.existsById(destRoomID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "The room does not exist");
        }
        Room srcRoom = roomRepo.findRoomByRoomID(srcRoomID);
        Room destRoom = roomRepo.findRoomByRoomID(destRoomID);
        // check size of new room
        int destRoomNum = destRoom.getRoomArtifacts().size() + 1;
        if (destRoom.getCapacity() > 0 && destRoomNum > destRoom.getCapacity()) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Display room's artifacts cannot exceed its capacity");
        }
        if (!artifactRepo.existsById(artifactID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "The artifact does not exist");
        }
        Artifact artifact = artifactRepo.findByArtID(artifactID);
        if (!srcRoom.getRoomArtifacts().contains(artifact) || destRoom.getRoomArtifacts().contains(artifact)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "The artifact conflicts with this movement");
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

    @Transactional
    public List<Room> removeArtifactsFromRooms(List<Artifact> removedArtifactList) {
        // get all artifacts rooms
        List<Room> roomList = new ArrayList<>();
        for (Artifact artifact: removedArtifactList) {
            int roomID = this.getAllRoomsAndArtifacts().get(artifact.getArtID());
            Room room = roomRepo.findRoomByRoomID(roomID);
            room.removeRoomArtifact(artifact);
            room = roomRepo.save(room);
            roomList.add(room);
        }
        return roomList;
    }

    @Transactional
    public Room createRoom(String roomName, int roomCapacity) {
//        if (roomCapacity > 0 && artifactIDList.size() > roomCapacity) {
//            throw new RuntimeException("Each loan can only contain maximum of 5 artifacts");
//        }
        Room room = new Room();
        room.setName(roomName);
        room.setCapacity(roomCapacity);
        room.setNewRoomArtifactsList();
//        for (int artifactID: artifactIDList) {
//            if (!artifactRepo.existsById(artifactID)) {
//                throw new DatabaseException(HttpStatus.NOT_FOUND, "Artifact for Room is not in database");
//            }
//            HashMap<Integer, Integer> allRoomsAndArtifacts = this.getAllRoomsAndArtifacts();
//
//            Artifact artifact = artifactRepo.findByArtID(artifactID);
//            room.addRoomArtifact(artifact);
//        }
        room = roomRepo.save(room);
        return room;
    }
}
