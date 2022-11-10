package com.example.museum.service;

import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.ArtifactRepositoryTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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
        final Artifact testArtifact = new Artifact(id, name, type, loanable, loaned);

        when(artifactRepository.findByArtID(id)).thenAnswer((InvocationOnMock invocation) -> testArtifact);

        Artifact artifact = artifactService.getArtifactByArtID(id);

        assertNotNull(artifact);
        assertEquals(artifact.getName(), testArtifact.getName());
        assertEquals(artifact.getType(), testArtifact.getType());
        assertEquals(artifact.getLoanable(), testArtifact.getLoanable());
        assertEquals(artifact.getLoaned(), testArtifact.getLoaned());


    }
}
