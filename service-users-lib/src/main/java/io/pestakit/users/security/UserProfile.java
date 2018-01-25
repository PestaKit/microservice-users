/**
 -----------------------------------------------------------------------------------
 Laboratoire : <nn>
 Fichier     : UserProfile.java
 Auteur(s)   : Andrea Cotza
 Date        : 13.12.2017
 
 But         : <‡ complÈter>
 
 Remarque(s) : <‡ complÈter>
 
 Compilateur : jdk1.8.0_60
 -----------------------------------------------------------------------------------
*/

package io.pestakit.users.security;

import java.util.UUID;

public class UserProfile {
    private UUID userId;
    private String username;

    public UserProfile(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

}
