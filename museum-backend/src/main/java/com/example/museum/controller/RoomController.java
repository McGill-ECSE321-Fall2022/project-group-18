package com.example.museum.controller;

import com.example.museum.dto.RoomDto;
import com.example.museum.model.Room;
import com.example.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/room/{roomID}")
    public ResponseEntity<RoomDto> getRoomByRoomID(@PathVariable int roomID) {
        Room room = roomService.getRoomByID(roomID);
        return new ResponseEntity<RoomDto>(new RoomDto(room), HttpStatus.OK);
    }

    @GetMapping("/room")
    public ResponseEntity<Integer> createRoom(@RequestParam String roomName, @RequestParam int roomCapacity) {
        Room room = roomService.createRoom(roomName, roomCapacity);
        RoomDto response = new RoomDto(room);
        return new ResponseEntity<Integer>(room.getRoomID(), HttpStatus.OK);
    }

    @GetMapping("/room/artifacts/add")
    public ResponseEntity<Integer> addArtifacts(@RequestParam int roomID, @RequestParam List<Integer> artifactIDList) {
        Room room = roomService.addArtifactsToRoom(roomID, artifactIDList);
        //RoomDto response = new RoomDto(room);
        return new ResponseEntity<Integer>(room.getRoomID(), HttpStatus.OK);
    }

    @PostMapping("/room/artifacts/move")
    public ResponseEntity<List<RoomDto>> moveArtifact(@RequestParam int srcRoomID, @RequestParam int destRoomID, @RequestParam int artifactID) {
        List<Room> roomList = roomService.transferArtifactBetweenRooms(srcRoomID, destRoomID, artifactID);
        List<RoomDto> roomDtoList = new ArrayList<>();
        roomDtoList.add(0, new RoomDto(roomList.get(0)));
        roomDtoList.add(1, new RoomDto(roomList.get(1)));
        return new ResponseEntity<List<RoomDto>>(roomDtoList, HttpStatus.OK);
    }

}
