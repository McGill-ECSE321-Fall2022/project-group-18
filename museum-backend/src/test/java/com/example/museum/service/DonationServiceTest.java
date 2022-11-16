package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import com.example.museum.repository.DonationRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTest {

    @Mock
    DonationRepository donationRepository;

    @InjectMocks
    DonationService donationService;

    @Test
    public void testGetDonationID(){
        final int id = 1;
        final Donation testDonation = new Donation(id);

        when(donationRepository.findDonationByDonationID(id)).thenAnswer((InvocationOnMock invocation) -> testDonation);

        Donation donation = donationService.getDonationByDonationID(id);

        assertNotNull(donation);


    }

    @Test
    public void testGetDonationIDNotInDB(){
        final int id = 1;
        final Artifact art1 = new Artifact(1,"Mona",Artifact.ArtType.Painting,false,false,1);

        Donation testDonation = new Donation();
        testDonation.addDonatedArtifact(art1);


        Exception ex = assertThrows(DatabaseException.class, () -> donationService.getDonationByDonationID(id));
    }

    @Test
    void testCreateDonation(){
        when(donationRepository.save(any(Donation.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final String name = "Mona Lisa";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = false;
        final boolean loaned = false;
        final int loanFee = 10;

        Artifact art = new Artifact();
        art.setName(name);
        art.setType(type);
        art.setLoanable(loanable);
        art.setLoaned(loaned);
        art.setLoanFee(loanFee);

        List<Artifact> listArts = new ArrayList<>();
        listArts.add(art);

        Donation returnedDonation = donationService.createDonation(listArts);

        assertNotNull(returnedDonation);
        assertEquals(name,returnedDonation.getDonatedArtifact(0).getName());
        assertEquals(type,returnedDonation.getDonatedArtifact(0).getType());
        assertEquals(loanable,returnedDonation.getDonatedArtifact(0).getLoanable());
        assertEquals(loaned,returnedDonation.getDonatedArtifact(0).getLoaned());
        assertEquals(loanFee,returnedDonation.getDonatedArtifact(0).getLoanFee());
    }

    @Test
    void testGetArtifactsInDonation(){
        final int id = 1;
        final Artifact art1 = new Artifact(1,"Mona",Artifact.ArtType.Painting,false,false,1);
        final Artifact art2 = new Artifact(2,"Lisa",Artifact.ArtType.Sculpture, true,true,2);


        Donation testDonation = new Donation();
        testDonation.addDonatedArtifact(art1);
        testDonation.addDonatedArtifact(art2);

        when(donationRepository.findDonationByDonationID(id)).thenAnswer((InvocationOnMock invocation) -> testDonation);

        List<Artifact> returnedArts = donationService.getArtifactsInDonation(id);

        assertNotNull(returnedArts);
        assertEquals(art1.getName(), returnedArts.get(0).getName());
        assertEquals(art1.getType(), returnedArts.get(0).getType());
        assertEquals(art1.getLoanable(), returnedArts.get(0).getLoanable());
        assertEquals(art1.getLoaned(), returnedArts.get(0).getLoaned());
        assertEquals(art1.getLoanFee(), returnedArts.get(0).getLoanFee());

        assertNotNull(returnedArts);
        assertEquals(art2.getName(), returnedArts.get(1).getName());
        assertEquals(art2.getType(), returnedArts.get(1).getType());
        assertEquals(art2.getLoanable(), returnedArts.get(1).getLoanable());
        assertEquals(art2.getLoaned(), returnedArts.get(1).getLoaned());
        assertEquals(art2.getLoanFee(), returnedArts.get(1).getLoanFee());
    }



}
