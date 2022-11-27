package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Business;
import com.example.museum.model.Owner;
import com.example.museum.model.Room;
import com.example.museum.repository.BusinessRepository;
import com.example.museum.repository.OwnerRepository;
import com.example.museum.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MuseumSetupServiceTests {
    @Mock
    RoomRepository roomRepository;
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    BusinessRepository businessRepository;

    @InjectMocks
    MuseumSetupService museumSetupService;

    @Test
    //successful test for when rooms don't already exist
    public void testCreateRooms(){
        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> new ArrayList<>());
        when(roomRepository.save(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        List<Room> rooms = museumSetupService.createRooms();

        assertNotNull(rooms);
        assertEquals(11, rooms.size());
        for(int i = 0; i < 5 ; i ++){
            int j = i + 1;
            assertEquals("SR" + j, rooms.get(i).getName());
            assertEquals(200, rooms.get(i).getCapacity());
            assertEquals(0, rooms.get(i).getRoomArtifacts().size());
        }
        for(int i = 0 ; i < 5 ; i ++){
            int j = i + 1;
            assertEquals("LR" + j, rooms.get(i+5).getName());
            assertEquals(300, rooms.get(i+5).getCapacity());
            assertEquals(0, rooms.get(i+5).getRoomArtifacts().size());
        }
        assertEquals("Storage", rooms.get(10).getName());
        assertEquals(Integer.MAX_VALUE, rooms.get(10).getCapacity());
        assertEquals(0, rooms.get(10).getRoomArtifacts().size());

        verify(roomRepository, times(11)).save(any(Room.class));
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    //test when rooms already exist (unsuccessful room initialization)
    public void testRoomsAlreadyExist(){
        List<Room> nonEmptyList = new ArrayList<>();
        nonEmptyList.add(new Room());
        when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> nonEmptyList);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> museumSetupService.createRooms());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    //successful creation of the owner
    public void testCreateOwner(){
        when(ownerRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> new ArrayList<>());
        when(ownerRepository.save(any(Owner.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Owner owner = museumSetupService.createOwner();

        assertNotNull(owner);
        assertEquals("owner321", owner.getUsername());
        assertEquals("ecse321", owner.getPassword());
        assertEquals("Marwan", owner.getFirstName());
        assertEquals("Kanaan", owner.getLastName());

        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    public void testOwnerAlreadyExists(){
        Owner existingOwner = new Owner();
        List<Owner> existingOwnerList = new ArrayList<>();
        existingOwnerList.add(existingOwner);
        when(ownerRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> existingOwnerList);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> museumSetupService.createOwner());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void testCreateBusiness(){
        when(businessRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> new ArrayList<>());
        when(businessRepository.save(any(Business.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Business business = museumSetupService.createBusiness();

        assertNotNull(business);
        assertEquals(10, business.getTicketFee());

        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    public void testBusinessAlreadyExists(){
        Business business = new Business();
        List<Business> existingBusinessList = new ArrayList<>();
        existingBusinessList.add(business);
        when(businessRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> existingBusinessList);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> museumSetupService.createBusiness());
        assertNotNull(ex);
        assertEquals("Business already exists in the database", ex.getMessage());
        verify(businessRepository, times(1)).findAll();

    }
}
