package com.example.museum.repository;

import com.example.museum.model.LoanedArtifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class LoanedArtifactRepositoryTests {
    @Autowired
    private LoanedArtifactRepository loanedArtifactRepository;

    @AfterEach
    public void clearDatabase() {
        loanedArtifactRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadLoanedArtifact() {
        // Create loanedArtifact Object
        int loanFee = 1000;
        LoanedArtifact loanedMonaLisa = new LoanedArtifact();
        loanedMonaLisa.setLoanFee(loanFee);

        // Save artifact

        loanedMonaLisa = loanedArtifactRepository.save(loanedMonaLisa);
        int id = loanedMonaLisa.getArtID();

        // Read object from database

        loanedMonaLisa = loanedArtifactRepository.findByArtID(id);

        // Assert that object has correct attributes

        assertNotNull(loanedMonaLisa);
        assertEquals(loanFee, loanedMonaLisa.getLoanFee());

    }
}
