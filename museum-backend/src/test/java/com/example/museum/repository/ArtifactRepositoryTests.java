package com.example.museum.repository;

import com.example.museum.model.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class ArtifactRepositoryTests {
    @Autowired
    private ArtifactRepository artifactRepository;

    @AfterEach
    public void clearDatabase() {
        artifactRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadArtifact() {
        boolean loanable = true;
        String name = "Mona Lisa";
        Artifact.ArtType type = Artifact.ArtType.Painting;

        Artifact artifact = new Artifact();
        artifact.setLoanable(loanable);
        artifact.setName(name);
        artifact.setType(type);


        artifact = artifactRepository.save(artifact);
        int id = artifact.getArtID();

        artifact = null;

        artifact = artifactRepository.findArtifactByArtID(id);

        assertNotNull(artifact);
        assertEquals(id, artifact.getArtID());
        assertEquals(loanable, artifact.getLoanable());
        assertEquals(name, artifact.getName());
        assertEquals(type, artifact.getType());

    }
}



