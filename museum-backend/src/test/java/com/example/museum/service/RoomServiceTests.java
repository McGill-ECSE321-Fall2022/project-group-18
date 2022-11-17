package com.example.museum.service;


import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Room;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {

    @Mock
    RoomRepository roomRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    RoomService roomService;

    @Test
    public void testGetRoomByID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoomID = 3;
        final String testRoomName = "Storage";
        final int testRoomCapacity = -1;

        Room testRoom = new Room();
        testRoom.setRoomID(testRoomID);
        testRoom.setName(testRoomName);
        testRoom.setCapacity(testRoomCapacity);
        testRoom.setNewRoomArtifactsList();
        testRoom.addRoomArtifact(testArtifact1);
        testRoom.addRoomArtifact(testArtifact2);

        when(roomRepository.existsById(testRoomID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoomID)).thenAnswer((InvocationOnMock invocation) -> testRoom);

        Room room = roomService.getRoomByID(testRoomID);
        assertNotNull(room);
        assertEquals(testRoomID, room.getRoomID());
        assertEquals(testRoomName, room.getName());
        assertEquals(testRoomCapacity, room.getCapacity());
        for (Artifact artifact: room.getRoomArtifacts()) {
            assertEquals(true, testArtifactIDSet.contains(artifact.getArtID()));
            if (artifact.getArtID() == artifact1ID) {
                assertEquals(artifact1Name, artifact.getName());
                assertEquals(artifact1Loanable, artifact.getLoanable());
                assertEquals(artifact1Loaned, artifact.getLoaned());
                assertEquals(artifact1Type, artifact.getType());
                assertEquals(artifact1LoanFee, artifact.getLoanFee());
            }
            if (artifact.getArtID() == artifact2ID) {
                assertEquals(artifact2Name, artifact.getName());
                assertEquals(artifact2Loanable, artifact.getLoanable());
                assertEquals(artifact2Loaned, artifact.getLoaned());
                assertEquals(artifact2Type, artifact.getType());
                assertEquals(artifact2LoanFee, artifact.getLoanFee());
            }
        }
    }

    @Test
    public void testGetRoomByIDWithInvalidRoomID() {
        int testRoomID = 123;
        when(roomRepository.existsById(testRoomID)).thenAnswer((InvocationOnMock invocation) -> false);
        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.getRoomByID(testRoomID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Room not found", ex.getMessage());
    }

    @Test
    public void testGetAllRooms() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);

        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();
        testRoom2.addRoomArtifact(testArtifact2);

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);
        List<Integer> testRoomIDList = new ArrayList<>();
        testRoomIDList.add(testRoom1.getRoomID());
        testRoomIDList.add(testRoom2.getRoomID());

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);

        List<Room> roomList = roomService.getAllRooms();
        assertNotNull(roomList);
        for (Room room: roomList) {
            assertEquals(true, testRoomIDList.contains(room.getRoomID()));
            if (room.getRoomID() == testRoom1ID) {
                assertEquals(testRoomName1, room.getName());
                assertEquals(testRoomCapacity1, room.getCapacity());
                assertEquals(testArtifact1.getArtID(), room.getRoomArtifact(0).getArtID());
            }
            if (room.getRoomID() == testRoom2ID) {
                assertEquals(testRoomName2, room.getName());
                assertEquals(testRoomCapacity2, room.getCapacity());
                assertEquals(testArtifact2.getArtID(), room.getRoomArtifact(0).getArtID());
            }
        }
    }

    @Test
    public void testGetAllRoomsAndArtifacts() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);

        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();
        testRoom2.addRoomArtifact(testArtifact2);

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);

        Map<Integer, Integer> allArtifactsAndRooms = roomService.getAllRoomsAndArtifacts();
        assertNotNull(allArtifactsAndRooms);
        assertEquals(true, allArtifactsAndRooms.containsKey(testArtifact1.getArtID()));
        assertEquals(true, allArtifactsAndRooms.containsKey(testArtifact2.getArtID()));
        assertEquals(testRoom1.getRoomID(), allArtifactsAndRooms.get(testArtifact1.getArtID()));
        assertEquals(testRoom2.getRoomID(), allArtifactsAndRooms.get(testArtifact2.getArtID()));
    }

    @Test
    public void testAddArtifactsToRoomWithRoomID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Room room = roomService.addArtifactsToRoom(testRoom1ID, testArtifactIDList);
        assertNotNull(room);
        assertEquals(2, room.getRoomArtifacts().size());
        for (Artifact artifact: room.getRoomArtifacts()) {
            if (artifact.getArtID() == artifact1ID) {
                assertEquals(artifact1Name, artifact.getName());
                assertEquals(artifact1Loanable, artifact.getLoanable());
                assertEquals(artifact1Loaned, artifact.getLoaned());
                assertEquals(artifact1Type, artifact.getType());
                assertEquals(artifact1LoanFee, artifact.getLoanFee());
            }
            if (artifact.getArtID() == artifact2ID) {
                assertEquals(artifact2Name, artifact.getName());
                assertEquals(artifact2Loanable, artifact.getLoanable());
                assertEquals(artifact2Loaned, artifact.getLoaned());
                assertEquals(artifact2Type, artifact.getType());
                assertEquals(artifact2LoanFee, artifact.getLoanFee());
            }
        }
    }

    @Test
    public void testAddArtifactsToRoomWithRoomIDAndInvalidRoomID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.addArtifactsToRoom(testRoom1ID, testArtifactIDList));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("This room does not exist", ex.getMessage());
    }


    @Test
    public void testAddArtifactsToRoomWithRoomIDAndExceededCapacity() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = 1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);

        RequestException ex = assertThrows(RequestException.class, () -> roomService.addArtifactsToRoom(testRoom1ID, testArtifactIDList));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Display room's artifacts cannot exceed its capacity", ex.getMessage());
    }

    @Test
    public void testAddArtifactsToRoomWithRoomIDAndNonexistArtifact() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> false);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);


        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.addArtifactsToRoom(testRoom1ID, testArtifactIDList));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Artifact for Room is not in database", ex.getMessage());
    }

    @Test
    public void testAddArtifactsToRoomWithRoomIDAndDuplicatedArtifact() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact1.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);

        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);



        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.addArtifactsToRoom(testRoom1ID, testArtifactIDList));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("This artifact already exists in rooms", ex.getMessage());
    }

    @Test
    public void testAddArtifactsToRoomWithRoomName() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Room room = roomService.addArtifactsToRoom(testRoomName1, testArtifactIDList);
        assertNotNull(room);
        assertEquals(2, room.getRoomArtifacts().size());
        for (Artifact artifact: room.getRoomArtifacts()) {
            if (artifact.getArtID() == artifact1ID) {
                assertEquals(artifact1Name, artifact.getName());
                assertEquals(artifact1Loanable, artifact.getLoanable());
                assertEquals(artifact1Loaned, artifact.getLoaned());
                assertEquals(artifact1Type, artifact.getType());
                assertEquals(artifact1LoanFee, artifact.getLoanFee());
            }
            if (artifact.getArtID() == artifact2ID) {
                assertEquals(artifact2Name, artifact.getName());
                assertEquals(artifact2Loanable, artifact.getLoanable());
                assertEquals(artifact2Loaned, artifact.getLoaned());
                assertEquals(artifact2Type, artifact.getType());
                assertEquals(artifact2LoanFee, artifact.getLoanFee());
            }
        }
    }

    @Test
    public void testAddArtifactsToRoomWithRoomNameAndWrongRoomName() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.existsByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.addArtifactsToRoom(testRoomName1, testArtifactIDList));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("This room does not exist", ex.getMessage());
    }

    @Test
    public void testAddArtifactsToRoomWithRoomNameAndExceededCapacity() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = 1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.existsByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> testRoom1);

        RequestException ex = assertThrows(RequestException.class, () -> roomService.addArtifactsToRoom(testRoomName1, testArtifactIDList));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Display room's artifacts cannot exceed its capacity", ex.getMessage());
    }

    @Test
    public void testAddArtifactsToRoomWithRoomNameAndNonexistArtifact() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.existsByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> false);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);


        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.addArtifactsToRoom(testRoomName1, testArtifactIDList));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Artifact for Room is not in database", ex.getMessage());
    }

    @Test
    public void testAddArtifactsToRoomWithRoomNameAndDuplicatedArtifact() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact1.getArtID());

        final int testRoom1ID = 3;
        final String testRoomName1 = "Storage";
        final int testRoomCapacity1 = -1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByName(testRoomName1)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);

        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);



        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.addArtifactsToRoom(testRoomName1, testArtifactIDList));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("This artifact already exists in rooms", ex.getMessage());
    }

    @Test
    public void testTransferArtifactBetweenRooms() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);
        testRoom1.addRoomArtifact(testArtifact2);

        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        List<Room> roomList = roomService.transferArtifactBetweenRooms(testRoom1ID, testRoom2ID, artifact1ID);
        assertNotNull(roomList);
        assertEquals(1, roomList.get(1).getRoomArtifacts().size());
        assertEquals(artifact1ID, roomList.get(1).getRoomArtifact(0).getArtID());
        assertEquals(artifact1Name, roomList.get(1).getRoomArtifact(0).getName());
        assertEquals(artifact1Loanable, roomList.get(1).getRoomArtifact(0).getLoanable());
        assertEquals(artifact1Loaned, roomList.get(1).getRoomArtifact(0).getLoaned());
        assertEquals(artifact1Type, roomList.get(1).getRoomArtifact(0).getType());
        assertEquals(artifact1LoanFee, roomList.get(1).getRoomArtifact(0).getLoanFee());

    }

    @Test
    public void testTransferArtifactBetweenRoomsWithInvalidRoomID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);
        testRoom1.addRoomArtifact(testArtifact2);

        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        //when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> false);
        //when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        //when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        //when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        //when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.transferArtifactBetweenRooms(testRoom1ID, testRoom2ID, artifact1ID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("The room does not exist", ex.getMessage());
    }

    @Test
    public void testTransferArtifactBetweenRoomsWithExceededCapacity() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 1;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);


        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();
        testRoom2.addRoomArtifact(testArtifact2);

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        //when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        //when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        //when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        RequestException ex = assertThrows(RequestException.class, () -> roomService.transferArtifactBetweenRooms(testRoom1ID, testRoom2ID, artifact1ID));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Display room's artifacts cannot exceed its capacity", ex.getMessage());
    }

    @Test
    public void testTransferArtifactBetweenRoomsWithNonexistArtifact() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);


        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();
        testRoom2.addRoomArtifact(testArtifact2);

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        //when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> false);
        //when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        //when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.transferArtifactBetweenRooms(testRoom1ID, testRoom2ID, artifact1ID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("The artifact does not exist", ex.getMessage());
    }

    @Test
    public void testTransferArtifactBetweenRoomsWithSameArtifactInDifferentRooms() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);


        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();
        testRoom2.addRoomArtifact(testArtifact1);

        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        //when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.transferArtifactBetweenRooms(testRoom1ID, testRoom2ID, artifact1ID));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("The artifact conflicts with this transfer", ex.getMessage());
    }

    @Test
    public void testRemoveArtifactsFromRooms() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Artifact> artifactList = new ArrayList<>();
        artifactList.add(testArtifact1);
        artifactList.add(testArtifact2);

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);
        testRoom1.addRoomArtifact(testArtifact2);

        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();


        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        //when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        //when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        //when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        //when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        List<Room> roomList = roomService.removeArtifactsFromRooms(artifactList);
        assertNotNull(roomList);
        assertEquals(0, roomList.get(0).getRoomArtifacts().size());
        assertEquals(0, roomList.get(1).getRoomArtifacts().size());
    }

    @Test
    public void testRemoveArtifactsFromRoomsWithArtifactsNotInRoom() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Artifact> artifactList = new ArrayList<>();
        artifactList.add(testArtifact1);
        artifactList.add(testArtifact2);

        final int testRoom1ID = 3;
        final int testRoom2ID = 4;
        final String testRoomName1 = "Storage";
        final String testRoomName2 = "LR1";
        final int testRoomCapacity1 = -1;
        final int testRoomCapacity2 = 300;

        Room testRoom1 = new Room();
        testRoom1.setRoomID(testRoom1ID);
        testRoom1.setName(testRoomName1);
        testRoom1.setCapacity(testRoomCapacity1);
        testRoom1.setNewRoomArtifactsList();
        testRoom1.addRoomArtifact(testArtifact1);

        Room testRoom2 = new Room();
        testRoom2.setRoomID(testRoom2ID);
        testRoom2.setName(testRoomName2);
        testRoom2.setCapacity(testRoomCapacity2);
        testRoom2.setNewRoomArtifactsList();


        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom1);
        testRoomList.add(testRoom2);

        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> testRoomList);
        //when(roomRepository.existsById(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(roomRepository.existsById(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(roomRepository.findRoomByRoomID(testRoom1ID)).thenAnswer((InvocationOnMock invocation) -> testRoom1);
        //when(roomRepository.findRoomByRoomID(testRoom2ID)).thenAnswer((InvocationOnMock invocation) -> testRoom2);
        //when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        //when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        //when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        DatabaseException ex = assertThrows(DatabaseException.class, () -> roomService.removeArtifactsFromRooms(artifactList));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("The artifact to remove is not found in any room", ex.getMessage());
    }

    @Test
    public void testCreateRoom() {
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        final String roomName = "Storage";
        final int roomCapacity = -1;
        Room room = roomService.createRoom(roomName, roomCapacity);
        assertNotNull(room);
        assertEquals(roomName, room.getName());
        assertEquals(roomCapacity, room.getCapacity());
    }
}
