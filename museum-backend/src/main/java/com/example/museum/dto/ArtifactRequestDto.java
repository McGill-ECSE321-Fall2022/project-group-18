package com.example.museum.dto;

import com.example.museum.model.Artifact;



public class ArtifactRequestDto {

    private int artID;
    private String name;
    private Artifact.ArtType type;
    private boolean loanable;
    private boolean loanded;

    public void setArtID(int artID){
        this.artID = artID;
    }

    public int getArtID(){
        return this.artID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setType(Artifact.ArtType type){
        this.type = type;
    }

    public Artifact.ArtType getType(){
        return this.type;
    }

    public void setLoanable(boolean loanable){
        this.loanable = loanable;
    }

    public boolean getLoanable(){
        return this.loanable;
    }

    public void setLoanded(boolean loanded){
        this.loanded = loanded;
    }

    public Artifact toModel(){
        Artifact artifact = new Artifact();
        artifact.setArtID(this.artID);
        artifact.setName(this.name);
        artifact.setType(this.type);
        artifact.setLoanable(this.loanable);
        artifact.setLoaned(this.loanded);
        return artifact;
    }


}

