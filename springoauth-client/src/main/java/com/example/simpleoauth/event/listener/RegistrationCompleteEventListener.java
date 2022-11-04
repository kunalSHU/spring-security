package com.example.simpleoauth.event.listener;

import com.example.simpleoauth.entity.User;
import com.example.simpleoauth.event.RegistrationCompleteEvent;
import com.example.simpleoauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString(); // random token for now, need to save it for the particular user
        userService.saveVerificationToken(token, user);

        // Send mail to user
        String url = event.getApplicationUrl() + "verifyRegistration?token=" + token;
        log.info("Click the link to verify your account: {}", url);
    }
}
