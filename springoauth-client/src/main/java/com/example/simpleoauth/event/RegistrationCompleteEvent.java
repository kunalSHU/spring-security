package com.example.simpleoauth.event;

import com.example.simpleoauth.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
