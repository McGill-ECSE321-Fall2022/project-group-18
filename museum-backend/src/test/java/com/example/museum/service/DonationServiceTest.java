// package com.example.museum.service;

// import com.example.museum.model.Donation;
// import com.example.museum.repository.ArtifactRepository;
// import com.example.museum.repository.DonationRepository;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.invocation.InvocationOnMock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class DonationServiceTest {

// @Mock
// DonationRepository donationRepository;

// @InjectMocks
// DonationService donationService;

// @Test
// public void testGetDonationID(){
// final int donationID = 1;
// final Donation testDonation = new Donation(donationID);

// when(donationRepository.findDonationByDonationID(donationID)).thenAnswer((InvocationOnMock
// invocation) -> testDonation);

// Donation donation = donationService.getDonationByDonationID(donationID);

// assertNotNull(donation);

// }

// @Test
// public void testCreateDonation(){

// when(donationRepository.save(any(Donation.class))).thenAnswer((InvocationOnMock
// invocation) -> invocation.getArgument(0));

// Donation donation = new Donation();

// Donation returnedDonation = donationService.createDonation(donation);

// assertNotNull(returnedDonation);

// verify(donationRepository, times(1)).save(donation);

// }
// }
