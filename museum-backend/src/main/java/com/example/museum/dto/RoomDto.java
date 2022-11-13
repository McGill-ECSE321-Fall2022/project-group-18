package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomDto {

    private int roomID;
    private String roomName;
    private int roomCapacity;
    //private List<ArtifactDto> artifactDtoList;
    private List<Integer> artifactIDList;

    public RoomDto (Room room) {
        this.roomID = room.getRoomID();
        this.roomName = room.getName();
        this.roomCapacity = room.getCapacity();
        this.artifactIDList = new ArrayList<>();
        for (Artifact artifact: room.getRoomArtifacts()) {
            this.artifactIDList.add(artifact.getArtID());
        }
    }

    public RoomDto () {}

    public int getRoomID() {
        return this.roomID;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public int getRoomCapacity() {
        return this.roomCapacity;
    }

    public List<Integer> getArtifactIDList() {
        return this.artifactIDList;
    }

    public Room toModel() {
        Room room = new Room();
        room.setRoomID(this.roomID);
        room.setCapacity(this.roomCapacity);
        room.setName(this.roomName);
        room.setNewRoomArtifactsList();
//        for (ArtifactDto artifactDto: this.artifactIDList) {
//            Artifact artifact = artifactDto.toModel();
//            room.addRoomArtifact(artifact);
//        }
        return room;
    }
}
