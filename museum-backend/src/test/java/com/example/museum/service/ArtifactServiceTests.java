package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtifactServiceTests {

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    ArtifactService artifactService;

    @Test
    public void testGetArtifactID(){
        final int id = 1;
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 87;
        final Artifact testArtifact = new Artifact(id, name, type, loanable, loaned, loanFee);

        when(artifactRepository.findByArtID(id)).thenAnswer((InvocationOnMock invocation) -> testArtifact);

        Artifact artifact = artifactService.getArtifactByArtID(id);

        assertNotNull(artifact);
        assertEquals(artifact.getName(), testArtifact.getName());
        assertEquals(artifact.getType(), testArtifact.getType());
        assertEquals(artifact.getLoanable(), testArtifact.getLoanable());
        assertEquals(artifact.getLoaned(), testArtifact.getLoaned());
        assertEquals(artifact.getLoanFee(), testArtifact.getLoanFee());

    }
    @Test
    public void testGetArtifactIDNotInDB(){
        final int id = 1;
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 87;
        final Artifact testArtifact = new Artifact(id, name, type, loanable, loaned, loanFee);


        Exception ex = assertThrows(DatabaseException.class, () -> artifactService.getArtifactByArtID(id));
    }

    @Test
    public void testCreateArtifact(){
        when(artifactRepository.save(any(Artifact.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final int artID = 10;
        final String name = "TheSculpture";
        final Artifact.ArtType type = Artifact.ArtType.Sculpture;
        final boolean loanable = false;
        final boolean loaned = false;
        final int loanFee = 10;


        Artifact artifact = new Artifact();
        artifact.setArtID(artID);
        artifact.setName(name);
        artifact.setType(type);
        artifact.setLoanable(loanable);
        artifact.setLoaned(loaned);
        artifact.setLoanFee(loanFee);

        Artifact returnedArtfact = artifactService.createArtifact(artifact);

        assertNotNull(returnedArtfact);
        assertEquals(name, returnedArtfact.getName());
        assertEquals(type, returnedArtfact.getType());
        assertEquals(loanable, returnedArtfact.getLoanable());
        assertEquals(loaned, returnedArtfact.getLoaned());
        assertEquals(loanFee, returnedArtfact.getLoanFee());

        verify(artifactRepository, times(1)).save(artifact);

    }

    @Test
    public void testCreateDuplicatedArtifact(){
        when(artifactRepository.save(any(Artifact.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final int artID = 10;
        final String name = "TheSculpture";
        final Artifact.ArtType type = Artifact.ArtType.Sculpture;
        final boolean loanable = false;
        final boolean loaned = false;
        final int loanFee = 10;


        Artifact artifact = new Artifact();
        artifact.setArtID(artID);
        artifact.setName(name);
        artifact.setType(type);
        artifact.setLoanable(loanable);
        artifact.setLoaned(loaned);
        artifact.setLoanFee(loanFee);

        Artifact returnedArtfact = artifactService.createArtifact(artifact);
        List<Artifact> allArtifactList = new ArrayList<>();
        allArtifactList.add(returnedArtfact);
        when(artifactRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> allArtifactList);

        Artifact artifact1 = new Artifact();
        artifact1.setArtID(artID + 1);
        artifact1.setName(name);
        artifact1.setType(type);
        artifact1.setLoaned(loaned);
        artifact1.setLoanable(loanable);
        artifact1.setLoanFee(loanFee);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> artifactService.createArtifact(artifact1));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Artifact with identical name and type exists in database", ex.getMessage());

    }

    @Test
    public void testUpdateArtifact(){

        final int artID = 1;
        final Artifact artifact1 = new Artifact(artID,"Mona Lisa",Artifact.ArtType.Painting, false, false, 1000);
        List<Artifact> arts = new ArrayList<>();
        arts.add(artifact1);

        when(artifactRepository.findByArtID(artID)).thenAnswer((InvocationOnMock invocation) -> artifact1);
        when(artifactRepository.save(any(Artifact.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final boolean loanable = true;
        final boolean loaned = true;
        final int loanFee = 1001;

        Artifact returnedArtifact = artifactService.updateArtifact(artID,loanable,loaned, loanFee);

        assertNotNull(returnedArtifact);
        assertEquals(loanable, returnedArtifact.getLoanable());
        assertEquals(loaned,returnedArtifact.getLoaned());
        assertEquals(loanFee, returnedArtifact.getLoanFee());

        verify(artifactRepository, times(1)).save(any(Artifact.class));

    }

    @Test
    void testGetAllArtifacts(){
        final Artifact artifact1 = new Artifact(1, "Piece of garbage, 1865", Artifact.ArtType.valueOf("Painting"), true, false, 40);
        final Artifact artifact2 = new Artifact(1, "Piece of crab, 1528", Artifact.ArtType.valueOf("Painting"), true, false, 40);
        List<Artifact> artifacts = new ArrayList<>();
        artifacts.add(artifact1);
        artifacts.add(artifact2);

        when(artifactRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> artifacts);

        List<Artifact> returnedArtifacts = artifactService.getAllArtifacts();

        assertNotNull(returnedArtifacts);
        assertEquals(artifact1.getArtID(), returnedArtifacts.get(0).getArtID());
        assertEquals(artifact2.getArtID(), returnedArtifacts.get(1).getArtID());
    }
}
