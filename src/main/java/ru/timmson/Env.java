package ru.timmson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public Integer getOtpAliveSeconds() {
        return otpAliveSeconds;
    }

    public Integer getTokenALiveDays() {
        return tokenALiveDays;
    }

    public String getTwilioAuthSid() {
        return twilioAuthSid;
    }

    public void setTwilioAuthSid(String twilioAuthSid) {
        this.twilioAuthSid = twilioAuthSid;
    }

    public String getTwilioAuthToken() {
        return twilioAuthToken;
    }

    public void setTwilioAuthToken(String twilioAuthToken) {
        this.twilioAuthToken = twilioAuthToken;
    }

    public String getTwilioFrom() {
        return twilioFrom;
    }

    public void setTwilioFrom(String twilioFrom) {
        this.twilioFrom = twilioFrom;
    }
}
