package com.example.GymInTheBack.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;
    @Column(name = "attachUrl")
    private String attachUrl;

    @Column(name = "redirectUrl")
    private String redirectUrl;
    @Column(name = "idEntityConcerned")
    private Long idEntityConcerned;
    @Column(name = "isRead")
    private Boolean readed;



    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Long getIdEntityConcerned() {
        return idEntityConcerned;
    }

    public void setIdEntityConcerned(Long idEntityConcerned) {
        this.idEntityConcerned = idEntityConcerned;
    }

    public Boolean getReaded() {
        return readed;
    }

    public void setReaded(Boolean readed) {
        this.readed = readed;
    }
}
