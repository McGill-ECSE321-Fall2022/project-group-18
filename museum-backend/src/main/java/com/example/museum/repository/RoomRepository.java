package com.example.museum.repository;

import com.example.museum.model.Room;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface RoomRepository extends CrudRepository<Room, Integer> {
    // DAO implementation here
    // using roomID to find Room object
    Room findRoomByRoomID(int id);
    // using room name to find room object
    Room findRoomByName(String name);
    // using room name to confirm if room object exists
    boolean existsByName(String name);
}
