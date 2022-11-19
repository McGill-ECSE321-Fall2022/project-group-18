package com.example.museum.service;


import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTests {

    @Mock
    LoanRepository loanRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    LoanService loanService;


    @Test
    public void testGetLoanByID() {
        // mock a loan in the persistence
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);

        Loan loan = loanService.getLoanByID(requestID);

        assertNotNull(loan);
        assertEquals(requestID, loan.getRequestID());
        assertEquals(loanFee, loan.getLoanFee());
        assertEquals(approval, loan.getApproved());
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            assertEquals(true, testArtifactIDSet.contains(artifact.getArtID()));
        }
    }

    @Test
    public void testGetLoanByInvalidID() {
        final int invalidID = 123;
        when(loanRepository.existsById(invalidID)).thenAnswer((InvocationOnMock invocation) -> false);
        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.getLoanByID(invalidID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    public void testGetArtifactsIDByLoanID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);

        List<Integer> artifactsIDList = loanService.getArtifactsIDByLoanID(requestID);

        assertNotNull(artifactsIDList);
        for (int artifactID: artifactsIDList) {
            assertEquals(true, testArtifactIDSet.contains(artifactID));
        }
    }

    @Test
    public void testGetArtifactsIDByInvalidLoanID() {
        final int invalidID = 321;
        when(loanRepository.existsById(invalidID)).thenAnswer((InvocationOnMock invocation) -> false);
        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.getLoanByID(invalidID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    public void testCreateLoan() {
        when(loanRepository.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);
        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);

        Loan loan = loanService.createLoan(testArtifactIDList);

        assertNotNull(loan);
        assertEquals(false, loan.getApproved());
        assertEquals(testArtifact1.getLoanFee()+testArtifact2.getLoanFee(), loan.getLoanFee());
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            assertEquals(true, testArtifactIDSet.contains(artifact.getArtID()));
            assertEquals(true, artifact.getLoanable());
            assertEquals(false, artifact.getLoaned());
        }
    }

    @Test
    public void testCreateLoanWithArtifactsBeyondLimit() {
        List<Integer> testArtifactIDList = new ArrayList<>();
        RequestException ex = assertThrows(RequestException.class, () -> loanService.createLoan(testArtifactIDList));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Each loan can only contain maximum of 5 artifacts", ex.getMessage());
    }

    @Test
    public void testCreateLoanWithNonexistArtifacts() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        final Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        final Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);

        final HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(artifact1ID);
        testArtifactIDSet.add(artifact1ID);

        final List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(artifact1ID);
        testArtifactIDList.add(artifact2ID);

        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> false);
        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.createLoan(testArtifactIDList));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Artifact for Loan is not in database", ex.getMessage());

    }

    @Test
    public void testCreateLoanWithWrongArtifactLoanableAttribute() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = false;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);

        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);


        RequestException ex = assertThrows(RequestException.class, () -> loanService.createLoan(testArtifactIDList));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Artifact for Loan must be loanable with a valid fee", ex.getMessage());
    }

    @Test
    public void testCreateLoanWithWrongArtifactLoanFeeAttribute() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = -200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);

        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);


        RequestException ex = assertThrows(RequestException.class, () -> loanService.createLoan(testArtifactIDList));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Artifact for Loan must be loanable with a valid fee", ex.getMessage());
    }

    @Test
    public void testCreateLoanWithWrongArtifactLoanedAttribute() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = true;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);

        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        List<Integer> testArtifactIDList = new ArrayList<>();
        testArtifactIDList.add(testArtifact1.getArtID());
        testArtifactIDList.add(testArtifact2.getArtID());

        when(artifactRepository.existsById(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.existsById(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(artifactRepository.findByArtID(artifact1ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact1);
        when(artifactRepository.findByArtID(artifact2ID)).thenAnswer((InvocationOnMock invocation) -> testArtifact2);

        RequestException ex = assertThrows(RequestException.class, () -> loanService.createLoan(testArtifactIDList));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Artifact for Loan must not be loaned", ex.getMessage());
    }

    @Test
    public void testSetLoanApprovalToTrue() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanRepository.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Loan loan = loanService.setLoanApprovalToTrue(requestID);
        assertNotNull(loan);
        assertEquals(requestID, loan.getRequestID());
        assertEquals(loanFee, loan.getLoanFee());
        assertEquals(true, loan.getApproved());
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            assertEquals(true, testArtifactIDSet.contains(artifact.getArtID()));
        }
    }

    @Test
    public void testSetLoanApprovalToTrueWithInvalidID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.setLoanApprovalToTrue(requestID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    public void testSetArtifactsInLoanToLoaned() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanRepository.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        when(artifactRepository.save(any(Artifact.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Loan loan = loanService.setArtifactsInLoanToLoaned(requestID);
        assertNotNull(loan);
        assertEquals(requestID, loan.getRequestID());
        assertEquals(loanFee, loan.getLoanFee());
        assertEquals(approval, loan.getApproved());
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            assertEquals(true, testArtifactIDSet.contains(artifact.getArtID()));
            if (artifact.getArtID() == 1) {
                assertEquals(true, artifact.getLoaned());
            }
            if (artifact.getArtID() == 2) {
                assertEquals(true, artifact.getLoaned());
            }
        }
    }

    @Test
    public void testSetArtifactsInLoanToLoanedWithInvalidLoanID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);
        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.setArtifactsInLoanToLoaned(requestID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    public void testSetArtifactsInLoanToUnloaned() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = true;
        final boolean artifact2Loaned = true;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanRepository.save(any(Loan.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        when(artifactRepository.save(any(Artifact.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Loan loan = loanService.setArtifactsInLoanToUnloaned(requestID);
        assertNotNull(loan);
        assertEquals(requestID, loan.getRequestID());
        assertEquals(loanFee, loan.getLoanFee());
        assertEquals(approval, loan.getApproved());
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            assertEquals(true, testArtifactIDSet.contains(artifact.getArtID()));
            if (artifact.getArtID() == 1) {
                assertEquals(false, artifact.getLoaned());
            }
            if (artifact.getArtID() == 2) {
                assertEquals(false, artifact.getLoaned());
            }
        }
    }

    @Test
    public void testSetArtifactsInLoanToUnloanedWithInvalidLoanID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = true;
        final boolean artifact2Loaned = true;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.setArtifactsInLoanToUnloaned(requestID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    public void testDeleteLoan() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        testArtifact1 = artifactRepository.save(testArtifact1);
        testArtifact2 = artifactRepository.save(testArtifact2);

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);

        loanService.deleteLoan(requestID);
        verify(loanRepository, times(0)).save(testLoan);

    }

    @Test
    public void testDeleteLoanWithInvalidID() {
        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());
        testArtifact1 = artifactRepository.save(testArtifact1);
        testArtifact2 = artifactRepository.save(testArtifact2);

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> loanService.setArtifactsInLoanToUnloaned(requestID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());

    }

}
