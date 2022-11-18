package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;
import com.example.museum.service.ArtifactService;

import java.util.ArrayList;
import java.util.List;

public class LoanDto {

    private int loanID;
    private int loanFee;
    private boolean loanApproval;
    //private List<ArtifactDto> artifactList;
    private List<Integer> artifactIDList;

    public LoanDto(Loan loan) {
        this.loanID = loan.getRequestID();
        this.loanFee = loan.getLoanFee();
        this.loanApproval = loan.getApproved();
        artifactIDList = new ArrayList<>();
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            this.artifactIDList.add(artifact.getArtID());
        }
    }

    public LoanDto() {}

    public int getLoanId() {
        return this.loanID;
    }

    public void setLoanID(int id) {
        this.loanID = id;
    }

    public int getLoanFee() {
        return this.loanFee;
    }

    public void setLoanFee(int newLoanFee) {
        this.loanFee = newLoanFee;
    }

    public boolean getLoanApproval() {
        return this.loanApproval;
    }

    public void setLoanApproval(boolean approval) {
        this.loanApproval = approval;
    }

    public void setLoanApprovalToTrue() {
        this.loanApproval = true;
    }

    public List<Integer> getArtifactList() {
        return this.artifactIDList;
    }

    public void setArtifactDtoList(List<Integer> artifactIDList) {
        this.artifactIDList = artifactIDList;
    }

    public Loan toModel() {
        Loan loan = new Loan();
        loan.setRequestID(this.loanID);
        loan.setLoanFee(this.loanFee);
        loan.setApproved(this.loanApproval);
        loan.setNewrequestedArtifactsList();
        return loan;
    }

}
