package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Collection;

@Entity(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "First Name is required.")
    @NotNull(message = "First Name is required.")
    @Column(name="first_name")
    private String firstName;

    @NotEmpty(message = "Last Name is required.")
    @NotNull(message = "Last Name is required.")
    @Column(name="last_name")
    private String lastName;

    @NotNull(message = "Email Address is required.")
    @NotEmpty(message = "Email Address is required.")
    @Email(message = "Email Address is not valid.")
    @Column(name="email", unique = true)
    private String email;

    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,68}$",
        message = "Password does not match the requirements."
    )
    @Column(name="password")
    private String password;
    @Column(name="enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(
                    name="user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name="role_id", referencedColumnName = "id"
            )
    )
    private Collection<Role> roles;

    // Constructors

    public User() {}

    public User(String firstName, String lastName, String email, String password, boolean enabled, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
