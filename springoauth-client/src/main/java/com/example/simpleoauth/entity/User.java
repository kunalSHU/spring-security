package com.example.simpleoauth.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;

    @Column(length = 60)
    private String password;
    private String email;
    private boolean enabled = false;
    private String role;
}
