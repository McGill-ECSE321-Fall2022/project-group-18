package com.example.museum.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Business;
import com.example.museum.model.Owner;
import com.example.museum.repository.BusinessRepository;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.EmployeeRepository;
import com.example.museum.repository.OwnerRepository;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTests {
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    BusinessRepository businessRepository;

    @InjectMocks
    OwnerService ownerService;
    @InjectMocks
    BusinessService businessService;

    @Test
    public void testGetOwnerID() {
        final int businessID = 1;
        final int ticketFee = 10;
        final Business testBusiness = new Business(businessID, ticketFee);

        final int ownerID = 2;
        final String username = "owner1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Owner testOwner = new Owner(ownerID, username, password, testBusiness, firstName, lastName);

        when(ownerRepository.findByAccountID(ownerID)).thenAnswer((InvocationOnMock invocation) -> testOwner);

        Owner owner = ownerService.getOwnerByID(ownerID);

        // Testing owner
        assertNotNull(owner);
        assertEquals(ownerID, owner.getAccountID());
        assertEquals(username, owner.getUsername());
        assertEquals(password, owner.getPassword());
        assertEquals(firstName, owner.getFirstName());
        assertEquals(lastName, owner.getLastName());
        // Testing associations
        assertEquals(owner.getBusiness().getBusinessID(), testOwner.getBusiness().getBusinessID());
        assertEquals(owner.getBusiness().getTicketFee(), testOwner.getBusiness().getTicketFee());
    }

    @Test
    void testGetOwnerByInvalidID() {
        final int invalidID = 11111;

        when(ownerRepository.findByAccountID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> ownerService.getOwnerByID(invalidID));

        assertEquals("Owner not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testCreateOwner() {
        when(ownerRepository.save(any(Owner.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        final int ticketFee = 10;
        final Business testBusiness = new Business();
        testBusiness.setTicketFee(ticketFee);

        final String username = "owner1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Owner testOwner = new Owner(0, username, password, testBusiness, firstName, lastName);

        Owner returnedOwner = ownerService.createOwner(testOwner);

        // Testing owner
        assertNotNull(returnedOwner);
        assertEquals(username, returnedOwner.getUsername());
        assertEquals(password, returnedOwner.getPassword());
        assertEquals(firstName, returnedOwner.getFirstName());
        assertEquals(lastName, returnedOwner.getLastName());
        // Testing associations
        assertEquals(testOwner.getBusiness().getBusinessID(), testOwner.getBusiness().getBusinessID());
        assertEquals(testOwner.getBusiness().getTicketFee(), testOwner.getBusiness().getTicketFee());
    }

    @Test
    void testCreateConflictingUsername() {
        final int ticketFee = 10;
        final Business testBusiness = new Business();
        testBusiness.setTicketFee(ticketFee);

        final String username = "owner1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Owner testOwner = new Owner(0, username, password, testBusiness, firstName, lastName);
        List<Owner> owners = new ArrayList<>();
        owners.add(testOwner);

        when(ownerRepository.findAll())
                .thenAnswer((InvocationOnMock invocation) -> owners);

        final int ownerID2 = 2;
        final String username2 = "owner1";
        final String password2 = "password";
        final String firstName2 = "Second";
        final String lastName2 = "User";
        final int credit2 = 10;
        final Owner testOwner2 = new Owner(credit2, username2, password2, testBusiness, firstName2, lastName2);

        Exception ex = assertThrows(DatabaseException.class, () -> ownerService.createOwner(testOwner2));
        verify(ownerRepository, times(0)).save(testOwner2);
    }

    @Test
    void testInvalidLoginOwner() {
        final int businessID = 1;
        final int ticketFee = 10;
        final Business testBusiness = new Business(businessID, ticketFee);

        final int ownerID = 1;
        final String username = "owner1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final int credit = 5;
        final Owner testOwner = new Owner(credit, username, password, testBusiness, firstName, lastName);

        Exception ex = assertThrows(DatabaseException.class, () -> ownerService.loginOwner(testOwner));
    }
}
