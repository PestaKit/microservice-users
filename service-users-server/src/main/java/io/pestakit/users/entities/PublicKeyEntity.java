package io.pestakit.users.entities;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class PublicKeyEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String publicKey;
    @Column(nullable = false, unique = true)
    private LocalDate dateCreation;
    @Column(nullable = false, unique = true)
    private LocalDate dateExpiration;



    public long getId() {
        return id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

}
