package com.argus.argus_server.model;

import com.argus.argus_server.security.PasswordHasher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class AppUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "userID")
    private Integer userID;

    @JsonIgnore // never send password hashes to the dashboard
    @Column (name = "password_hash", length = 255)
    private String passwordHash;

    @Column (name = "name", length = 100)
    private String name;

    @Column (name = "email", unique = true, length = 100)
    private String email;

    @Column (name = "role", length = 50)
    private String role;

    public AppUser() {
    }

    /** Class diagram User.login(): true if the password matches this user's stored hash. */
    public boolean login(String password) {
        if (password == null || password.length() > 100) {
            return false; // hashing a huge "password" is slow, so don't even try
        }
        return PasswordHasher.matches(password, passwordHash);
    }

    /** Stores a salted hash of the password */
    public void setPassword(String password) {
        this.passwordHash = PasswordHasher.hash(password);
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}