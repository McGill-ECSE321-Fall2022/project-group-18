package com.example.museum.controller;

import com.example.museum.dto.RoomDto;
import com.example.museum.model.Room;
import com.example.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/room/{roomID}")
    public ResponseEntity<RoomDto> getRoomByRoomID(@PathVariable int roomID) {
        Room room = roomService.getRoomByID(roomID);
        return new ResponseEntity<RoomDto>(new RoomDto(room), HttpStatus.OK);
    }

    @PostMapping("/room")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        Room room = roomDto.toModel();
        room = roomService.createRoom(room);
        RoomDto response = new RoomDto(room);
        return new ResponseEntity<RoomDto>(response, HttpStatus.OK);
    }
}
