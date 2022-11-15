package com.example.museum.service;

import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void testCreateArtifact(){
        when(artifactRepository.save(any(Artifact.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final int artID = 10;
        final String name = "TheSculpture";
        final Artifact.ArtType type = Artifact.ArtType.Sculpture;
        final boolean loanable = false;
        final boolean loaned = false;

        Artifact artifact = new Artifact();
        artifact.setArtID(artID);
        artifact.setName(name);
        artifact.setType(type);
        artifact.setLoanable(loanable);
        artifact.setLoaned(loaned);

        Artifact returnedArtfact = artifactService.createArtifact(artifact);

        assertNotNull(returnedArtfact);
        assertEquals(name, returnedArtfact.getName());
        assertEquals(type, returnedArtfact.getType());
        assertEquals(loanable, returnedArtfact.getLoanable());
        assertEquals(loaned, returnedArtfact.getLoaned());

        verify(artifactRepository, times(1)).save(artifact);

    }
}
