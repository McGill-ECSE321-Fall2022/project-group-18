package com.example.museum.repository;

import com.example.museum.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Room findRoomByRoomID(int id);
}
