package com.example.simpleoauth.service;

import com.example.simpleoauth.entity.PasswordResetToken;
import com.example.simpleoauth.entity.User;
import com.example.simpleoauth.entity.VerificationToken;
import com.example.simpleoauth.model.UserDao;
import com.example.simpleoauth.repository.PasswordResetTokenRepository;
import com.example.simpleoauth.repository.UserRepository;
import com.example.simpleoauth.repository.VerificationTokenRepository;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User registerUser(UserDao userDao) {
        System.out.println("This is userDao " + userDao);
        User user = new User();
        user.setEmail(userDao.getEmail());
        user.setFirstName(userDao.getFirstName());
        user.setLastName(userDao.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userDao.getPassword()));
        System.out.println("This is user " + user);
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }

        Calendar cal = Calendar.getInstance();

        // Check for expiration
        if (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true); // account has been activated
        userRepository.save(user);
        return "valid";
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void generatePasswordResetToken(User user, String token) {
        // generate a new token for the user
        // and save that token
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validateResetPasswordToken(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        //check for expiry
        Calendar cal = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
            return "expired";
        }

        // save password
        User user = passwordResetToken.getUser();

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            User currUser = optionalUser.get();
            currUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currUser);
            return "valid";
        }
        return null;
     }
}
