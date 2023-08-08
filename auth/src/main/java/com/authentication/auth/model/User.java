package com.authentication.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public String name;

    @JsonIgnore
    public String password;

    public User( String name, String password, String email, String role)
    {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String email;
    public String role;

    public User()
    {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
