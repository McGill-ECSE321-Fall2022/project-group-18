package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.model.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDto {

    private int loanID;
    private int loanFee;
    private boolean loanApproval;
    private List<ArtifactDto> artifactList;

    public LoanDto(Loan loan) {
        this.loanID = loan.getRequestID();
        this.loanFee = loan.getLoanFee();
        this.loanApproval = loan.getApproved();
        artifactList = new ArrayList<>();
        for (Artifact artifact: loan.getRequestedArtifacts()) {
            this.artifactList.add(new ArtifactDto((artifact)));
        }
    }

    public LoanDto() {}

    public int getLoanId() {
        return this.loanID;
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

    public void setLoanApprovalToTrue() {
        this.loanApproval = true;
    }

    public List<ArtifactDto> getArtifactList() {
        return this.artifactList;
    }

    public void setArtifactDtoList(List<ArtifactDto> artifactDtoList) {
        this.artifactList = artifactDtoList;
    }

    public Loan toModel() {
        Loan loan = new Loan();
        loan.setRequestID(this.loanID);
        loan.setLoanFee(this.loanFee);
        loan.setApproved(this.loanApproval);
        loan.setNewrequestedArtifactsList();
        for (ArtifactDto artifact: this.artifactList) {
            Artifact artifactModel = artifact.toModel();
            loan.addRequestedArtifact(artifactModel);
        }
        return loan;
    }

}
