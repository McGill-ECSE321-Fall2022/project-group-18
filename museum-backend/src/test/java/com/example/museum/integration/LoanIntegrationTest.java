package com.example.museum.integration;


import com.example.museum.dto.ArtifactDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;
import com.example.museum.model.Room;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.LoanRepository;
import com.example.museum.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loanRepository.deleteAll();
        roomRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    @Test
    public void testCreateAndApproveLoan() {
        List<Artifact> createArtifactList = createArtifact();
        Room room = createRoom();
        room = addArtifactToRoom(room.getRoomID(), createArtifactList);
        int id = testCreateAndGetLoan(createArtifactList);
        testApproveLoan(id, room.getRoomID());
    }

    private List<Artifact> createArtifact() {
        final String name1 = "Mona Lisa";
        final Artifact.ArtType type1 = Artifact.ArtType.Painting;
        final boolean loanable1 = true;
        final boolean loaned1 = false;
        final int loanFee1 = 100;
        Artifact artifact1 = new Artifact();
        artifact1.setName(name1);
        artifact1.setType(type1);
        artifact1.setLoanable(loanable1);
        artifact1.setLoaned(loaned1);
        artifact1.setLoanFee(loanFee1);
        ArtifactDto artifactDto1 = new ArtifactDto(artifact1);
        ResponseEntity<ArtifactDto> response1 = client.postForEntity("/artifact", artifactDto1, ArtifactDto.class);

        List<Artifact> artifactList = new ArrayList<>();
        artifactList.add(response1.getBody().toModel());

        final String name2 = "David";
        final Artifact.ArtType type2 = Artifact.ArtType.Sculpture;
        final boolean loanable2 = true;
        final boolean loaned2 = false;
        final int loanFee2 = 120;
        Artifact artifact2 = new Artifact();
        artifact2.setName(name2);
        artifact2.setType(type2);
        artifact2.setLoanable(loanable2);
        artifact2.setLoaned(loaned2);
        artifact2.setLoanFee(loanFee2);
        ArtifactDto artifactDto2 = new ArtifactDto(artifact2);
        ResponseEntity<ArtifactDto> response2 = client.postForEntity("/artifact", artifactDto2, ArtifactDto.class);
        artifactList.add(response2.getBody().toModel());
        return artifactList;
    }

    private Room createRoom() {
        final int roomCapacity = 300;
        final String roomName = "LR2";
        String getParam = "";
        getParam = getParam + "?roomName=";
        getParam = getParam + roomName;
        getParam = getParam + "&roomCapacity=";
        getParam = getParam + roomCapacity;
        ResponseEntity<Integer> response = client.getForEntity("/room" + getParam, Integer.class);
        Room room = roomRepository.findRoomByRoomID(response.getBody());
        return room;
    }

    private Room addArtifactToRoom(int roomID, List<Artifact> artifactList) {
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: artifactList) {
            artifactIDList.add(artifact.getArtID());
        }
        String getParam = "/room/artifacts/add";
        getParam += "?roomID=";
        getParam += roomID;
        getParam += "&artifactIDList=";
        for (int i = 0; i < artifactIDList.size(); i++) {
            getParam = getParam + artifactIDList.get(i).toString();
            if (i == artifactIDList.size()-1) {
                continue;
            }
            getParam = getParam + ",";
        }
        ResponseEntity<Integer> response = client.getForEntity(getParam, Integer.class);
        Room room = roomRepository.findRoomByRoomID(response.getBody());
        return room;
    }

    private int testCreateAndGetLoan(List<Artifact> artifactList) {
        List<Integer> artifactIDList = new ArrayList<>();
        int artifactFeeSum = 0;
        for (Artifact artifact: artifactList) {
            artifactIDList.add(artifact.getArtID());
            artifactFeeSum += artifact.getLoanFee();
        }
        String artifactIDStr = "?artifactIDList=";
        for (int i = 0; i < artifactIDList.size(); i++) {
            artifactIDStr = artifactIDStr + artifactIDList.get(i).toString();
            if (i == artifactIDList.size()-1) {
                continue;
            }
            artifactIDStr = artifactIDStr + ",";
        }
        ResponseEntity<Integer> response = client.getForEntity("/loan" + artifactIDStr, Integer.class);
        assertNotNull(response.getBody());
        Loan loan = loanRepository.findLoanRequestByRequestID(response.getBody());
        assertEquals(artifactFeeSum, loan.getLoanFee());
        assertFalse(loan.getApproved());
        assertEquals(artifactList.size(), loan.getRequestedArtifacts().size());

        String loanGetParam = "/loan/";
        loanGetParam += response.getBody().toString();
        ResponseEntity<Map> responseGet = client.getForEntity(loanGetParam, Map.class);
        //System.out.println(responseGet.getBody().toString());

        assertNotNull(responseGet.getBody());
        assertEquals(loan.getLoanFee(), responseGet.getBody().get("loanFee"));
        assertEquals(loan.getRequestID(), responseGet.getBody().get("loanID"));
        assertEquals(loan.getApproved(), responseGet.getBody().get("loanStatus"));
        List<Integer> getArtifactIDList = (List<Integer>) responseGet.getBody().get("loanArtifactIDList");
        assertEquals(loan.getRequestedArtifacts().size(), getArtifactIDList.size());
        for (int id: getArtifactIDList) {
            assertTrue(artifactIDList.contains(id));
        }

        return loan.getRequestID();
    }

    public void testApproveLoan(int loanID, int roomID) {
        // test if the approval loan can 1. change the loan approval status
        //                               2. change the loaned status of artifacts
        //                               3. move artifacts out of their room
        Loan loan = loanRepository.findLoanRequestByRequestID(loanID);
        String loanGetParam = "/loan/update/approve?loanID=";
        loanGetParam += loanID;
        ResponseEntity<Map> response = client.getForEntity(loanGetParam, Map.class);
        assertNotNull(response.getBody());
        assertEquals(loan.getRequestID(), response.getBody().get("loanID"));
        assertEquals(true, response.getBody().get("loanStatus"));
        assertEquals(loan.getLoanFee(), response.getBody().get("loanFee"));
        List<Integer> getArtifactIDList = (List<Integer>) response.getBody().get("loanArtifactIDList");
        assertEquals(loan.getRequestedArtifacts().size(), getArtifactIDList.size());
        Room room = roomRepository.findRoomByRoomID(roomID);
        for (int artifactID: getArtifactIDList) {
            Artifact artifact = artifactRepository.findByArtID(artifactID);
            assertTrue(artifact.getLoaned());
            assertFalse(room.getRoomArtifacts().contains(artifact));
        }

    }
}
