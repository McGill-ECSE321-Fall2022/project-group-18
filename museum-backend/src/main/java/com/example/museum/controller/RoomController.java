package com.example.museum.controller;

import com.example.museum.model.Artifact;
import com.example.museum.model.Room;
import com.example.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    // get the information of a room in a map
    @GetMapping("/room/{roomID}")
    public ResponseEntity<Map<String, Object>> getRoomByRoomID(@PathVariable int roomID) {
        Room room = roomService.getRoomByID(roomID);
        Map<String, Object> response = new HashMap<>();
        response.put("roomID", room.getRoomID());
        response.put("roomCapacity", room.getCapacity());
        response.put("roomName", room.getName());
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact : room.getRoomArtifacts()) {
            artifactIDList.add(artifact.getArtID());
        }
        response.put("roomArtifactIDList", artifactIDList);
        response.put("roomArtifactNum", artifactIDList.size());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    // get all rooms in a ID list
    @GetMapping("/room/all")
    public ResponseEntity<List<Integer>> getAllRoomsID() {
        List<Integer> roomIDList = roomService.getAllRoomsID();
        return new ResponseEntity<List<Integer>>(roomIDList, HttpStatus.OK);
    }

    // get a Map<artifactID, roomID> for all artifacts in rooms
    @GetMapping("/room/all/artifacts")
    public ResponseEntity<Map<String, Integer>> getAllRoomsArtifactsID() {
        Map<Integer, Integer> roomArtifactsIDMap = roomService.getAllRoomsAndArtifacts();
        Map<String, Integer> anotherRoomArtifactsIDMap = new HashMap<>();
        roomArtifactsIDMap.forEach((key, value) -> {
            String keyInStr = key.toString();
            anotherRoomArtifactsIDMap.put(keyInStr, value);
        });
        return new ResponseEntity<Map<String, Integer>>(anotherRoomArtifactsIDMap, HttpStatus.OK);
    }

    // create an empty room using name and capacity
    @GetMapping("/room")
    public ResponseEntity<Integer> createRoom(@RequestParam String roomName, @RequestParam int roomCapacity) {
        Room room = roomService.createRoom(roomName, roomCapacity);
        return new ResponseEntity<Integer>(room.getRoomID(), HttpStatus.OK);
    }

    // add a list of artifacts into a room. These artifacts should not be any rooms
    // before this action
    @GetMapping("/room/artifacts/add")
    public ResponseEntity<Map<String, Object>> addArtifacts(@RequestParam int roomID,
            @RequestParam List<Integer> artifactIDList) {
        Room room = roomService.addArtifactsToRoom(roomID, artifactIDList);
        Map<String, Object> response = new HashMap<>();
        response.put("roomID", room.getRoomID());
        response.put("roomName", room.getName());
        response.put("roomCapacity", room.getCapacity());
        response.put("roomArtifactNum", room.getRoomArtifacts().size());
        List<Integer> roomArtifactIDList = new ArrayList<>();
        for (Artifact artifact : room.getRoomArtifacts()) {
            roomArtifactIDList.add(artifact.getArtID());
        }
        response.put("roomArtifactIDList", roomArtifactIDList);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    // move 1 artifact from one room to another. The rooms ID and artifact ID is
    // needed.
    @GetMapping("/room/artifacts/move")
    public ResponseEntity<Map<String, Object>> moveArtifact(@RequestParam int srcRoomID, @RequestParam int destRoomID,
            @RequestParam int artifactID) {
        List<Room> roomList = roomService.transferArtifactBetweenRooms(srcRoomID, destRoomID, artifactID);
        Map<String, Object> response = new HashMap<>();
        response.put("srcRoomID", roomList.get(0).getRoomID());
        response.put("srcRoomName", roomList.get(0).getName());
        response.put("srcRoomCapacity", roomList.get(0).getCapacity());
        response.put("srcRoomArtifactNum", roomList.get(0).getRoomArtifacts().size());
        List<Integer> srcRoomArtifactIDList = new ArrayList<>();
        List<Integer> destRoomArtifactIDList = new ArrayList<>();
        for (Artifact artifact : roomList.get(0).getRoomArtifacts()) {
            srcRoomArtifactIDList.add(artifact.getArtID());
        }
        for (Artifact artifact : roomList.get(1).getRoomArtifacts()) {
            destRoomArtifactIDList.add(artifact.getArtID());
        }
        response.put("srcRoomArtifactIDList", srcRoomArtifactIDList);
        response.put("destRoomID", roomList.get(1).getRoomID());
        response.put("destRoomName", roomList.get(1).getName());
        response.put("destRoomCapacity", roomList.get(1).getCapacity());
        response.put("destRoomArtifactNum", roomList.get(1).getRoomArtifacts().size());
        response.put("destRoomArtifactIDList", destRoomArtifactIDList);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
