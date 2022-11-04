package com.example.simpleoauth.model;

import lombok.Data;

@Data
public class ResetPasswordDao {
    private String email;
    private String oldPassword;
    private String newPassword;
}
