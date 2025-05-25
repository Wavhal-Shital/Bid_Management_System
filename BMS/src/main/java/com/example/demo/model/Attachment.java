package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Attachment {

     @Id
     @GeneratedValue
    private Long id;
    @ManyToOne
    private Email email;
    private String fileName;
    private String mimeType;
    private String driveLink;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public String getDriveLink() {
        return driveLink;
    }
    public void setDriveLink(String driveLink) {
        this.driveLink = driveLink;
    }
    

}
