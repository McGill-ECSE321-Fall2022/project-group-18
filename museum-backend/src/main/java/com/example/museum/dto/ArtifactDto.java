package com.example.museum.dto;

import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;

public class ArtifactDto {
    private int artID;
    private String name;
    private Artifact.ArtType type;
    private boolean loanable;
    private boolean loanded;

    public ArtifactDto(Artifact artifact){
        this.artID = artifact.getArtID();
        this.name = artifact.getName();
        this.type = artifact.getType();
        this.loanable = artifact.getLoanable();;
        this.loanded = artifact.getLoaned();
    }

    public int getArtID(){
        return this.artID;
    }

    public String getName(){
        return this.name;
    }

    public Artifact.ArtType getType(){
        return this.type;
    }

    public boolean getLoanable(){
        return this.loanable;
    }

    public boolean getLoaned(){
        return this.loanded;
    }

}
