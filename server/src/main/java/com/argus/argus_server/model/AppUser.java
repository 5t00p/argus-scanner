package com.argus.argus_server.model;
import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class AppUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "userID")
    private Integer userID;

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

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

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