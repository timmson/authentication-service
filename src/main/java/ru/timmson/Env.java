package ru.timmson;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Service
public class Env {

    @Value("${auth.otp.aliveSeconds}")
    private final Integer otpAliveSeconds = 60;
    @Value("${auth.token.aliveDays}")
    private final Integer tokenALiveDays = 14;
    @Value("${twilio.auth.sid}")
    private String twilioAuthSid;
    @Value("${twilio.auth.token}")
    private String twilioAuthToken;
    @Value("${twilio.from}")
    private String twilioFrom;

}
