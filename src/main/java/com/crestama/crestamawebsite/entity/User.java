package com.crestama.crestamawebsite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="users")
public class User {
    // Attribute Fields
    @Id
    @Column(name="username")
    @NotNull(message="Username is required.")
    @NotEmpty(message="Username is required.")
    private String username;

    @Column(name="password")
    @NotNull(message="Password is required.")
    @NotEmpty(message="Password is required.")
    private String password;

    @Column(name="enabled")
    private Boolean enabled;

    // Constructors
    public User() {

    }

    public User(String username, String password) {
        set(username, password, true);
    }

    // Getters and setters

    public void set(String username, String password, Boolean enabled) {
        setUsername(username);
        setPassword(password);
        setEnabled(enabled);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
