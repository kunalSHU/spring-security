package com.example.simpleoauth.controller;

import com.example.simpleoauth.entity.User;
import com.example.simpleoauth.event.RegistrationCompleteEvent;
import com.example.simpleoauth.model.ResetPasswordDao;
import com.example.simpleoauth.model.UserDao;
import com.example.simpleoauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RefreshScope
@RestController
@Slf4j
public class Controller {

    @Value("${message: Default Hello}")
    private String message;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/access")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("My resource has been accessed through Okta access token");
    }

    @PostMapping(value = {"/register"}, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> register(@RequestBody UserDao userDao, HttpServletRequest httpServletRequest) {
        User user = userService.registerUser(userDao);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(httpServletRequest)));
        return ResponseEntity.ok("User has been created");
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody ResetPasswordDao resetPassword, HttpServletRequest httpServletRequest) {
        User user = userService.findUserByEmail(resetPassword.getEmail());
        String url = null;
        // generate a new token for the user
        // and save that token
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.generatePasswordResetToken(user, token);
            url = applicationUrl(httpServletRequest) + "/savePassword?token=" + token;
            log.info("S   {} ", url);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody ResetPasswordDao resetPassword) {
        // verify token
        String result = userService.validateResetPasswordToken(token, resetPassword.getNewPassword());
        if ("valid".equalsIgnoreCase(result)) {
            return "User Password Reset";
        }
        return "Cannot reset password";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        log.info("Result: {} ", result);
        if ("valid".equalsIgnoreCase(result)) {
            return "User verified";
        }
        return "Bad User";
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + "/"
                + httpServletRequest.getContextPath();
    }

    @GetMapping("/message")
    public String getMessage() {
        return message;
    }
}