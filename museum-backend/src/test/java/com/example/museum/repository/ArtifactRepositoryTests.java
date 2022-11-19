package com.example.museum.repository;

import com.example.museum.model.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of ArtifactRepo
// Springboot testing setup
@SpringBootTest
public class ArtifactRepositoryTests {

    // Injecting the artifactRepository
    @Autowired
    private ArtifactRepository artifactRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        artifactRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadArtifact() {
        boolean loanable = true;
        String name = "Mona Lisa";
        Artifact.ArtType type = Artifact.ArtType.Painting;
        // Create an artiface with some attributes
        Artifact artifact = new Artifact();
        artifact.setLoanable(loanable);
        artifact.setName(name);
        artifact.setType(type);
        // Save the artifact in its database, tests its writing
        artifact = artifactRepository.save(artifact);
        // Keep track with its automatically generated ID
        int id = artifact.getArtID();

        artifact = null;

        // Read from the database using the id, test its reading
        artifact = artifactRepository.findByArtID(id);
        // Test if an object is returned
        assertNotNull(artifact);
        // Test if the reference (id) read from DB is correct
        assertEquals(id, artifact.getArtID());
        // Test if the attributes read from DB is correct
        assertEquals(loanable, artifact.getLoanable());
        assertEquals(name, artifact.getName());
        assertEquals(type, artifact.getType());

    }
}


