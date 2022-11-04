package com.example.simpleoauth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int duration) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, duration);
        return new Date(cal.getTime().getTime());
    }

}
