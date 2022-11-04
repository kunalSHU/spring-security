package com.example.simpleoauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {

    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
