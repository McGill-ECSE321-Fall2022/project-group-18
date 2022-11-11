package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomDto {

    private int roomID;
    private String roomName;
    private int roomCapacity;
    private List<ArtifactDto> artifactDtoList;

    public RoomDto (Room room) {
        this.roomID = room.getRoomID();
        this.roomName = room.getName();
        this.roomCapacity = room.getCapacity();
        artifactDtoList = new ArrayList<>();
        for (Artifact artifact: room.getRoomArtifacts()) {
            this.artifactDtoList.add(new ArtifactDto(artifact));
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

    public List<ArtifactDto> getArtifactDtoList() {
        return this.artifactDtoList;
    }

    public Room toModel() {
        Room room = new Room();
        room.setRoomID(this.roomID);
        room.setCapacity(this.roomCapacity);
        room.setName(this.roomName);
        room.setNewRoomArtifactsList();
        for (ArtifactDto artifactDto: this.artifactDtoList) {
            Artifact artifact = artifactDto.toModel();
            room.addRoomArtifact(artifact);
        }
        return room;
    }
}
