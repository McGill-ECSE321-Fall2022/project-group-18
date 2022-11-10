package com.example.museum.dto;

import com.example.museum.model.Artifact;

public class ArtifactDto {

        private int artID;
        private String name;
        private Artifact.ArtType type;
        private boolean loanable;
        private boolean loaned;


        public ArtifactDto(Artifact artifact){
            this.artID = artifact.getArtID();
            this.name = artifact.getName();
            this.type = artifact.getType();
            this.loanable = artifact.getLoanable();
            this.loaned = artifact.getLoaned();
        }

        public ArtifactDto(){}



        public int getArtID(){
            return artID;
        }
        public String getName(){
            return name;
        }
        public Artifact.ArtType getType(){
            return type;
        }
        public boolean getLoanable(){
            return loanable;
        }
        public boolean getLoaned(){
            return loaned;
        }

        public Artifact toModel(){
            Artifact artifact = new Artifact();
            artifact.setArtID(this.getArtID());
            artifact.setName(this.getName());
            artifact.setType(this.getType());
            artifact.setLoanable(this.getLoanable());
            artifact.setLoaned(this.getLoaned());
            return artifact;
        }
}
