package com.example.simpleoauth.service;

import com.example.simpleoauth.entity.User;
import com.example.simpleoauth.model.UserDao;
import org.springframework.stereotype.Service;

public interface UserService {

    User registerUser(UserDao userDao);

    void saveVerificationToken(String token, User user);

    String validateVerificationToken(String token);

    User findUserByEmail(String email);

    void generatePasswordResetToken(User user, String token);

    String validateResetPasswordToken(String token, String newPassword);
}
