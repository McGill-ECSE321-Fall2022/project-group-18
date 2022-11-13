package com.example.museum.controller;

import com.example.museum.dto.RoomDto;
import com.example.museum.model.Room;
import com.example.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/room")
    public ResponseEntity<RoomDto> createRoom(@RequestParam String roomName, @RequestParam int roomCapacity) {
        Room room = roomService.createRoom(roomName, roomCapacity);
        RoomDto response = new RoomDto(room);
        return new ResponseEntity<RoomDto>(response, HttpStatus.OK);
    }
}
