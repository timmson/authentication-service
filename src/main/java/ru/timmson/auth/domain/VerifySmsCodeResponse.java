package ru.timmson.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifySmsCodeResponse implements AuthServiceResponse {

    private String errorMessage;

    private String uuid;

    private String accessToken;

    private Long accessTokenTtl;

    private String refreshToken;

    private Long refreshTokenTtl;

    private Integer remainingAttempts;

    private Long nextAttemptInterval;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getAccessTokenTtl() {
        return accessTokenTtl;
    }

    public void setAccessTokenTtl(Long accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenTtl() {
        return refreshTokenTtl;
    }

    public void setRefreshTokenTtl(Long refreshTokenTtl) {
        this.refreshTokenTtl = refreshTokenTtl;
    }

    public Integer getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(Integer remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    public Long getNextAttemptInterval() {
        return nextAttemptInterval;
    }

    public void setNextAttemptInterval(Long nextAttemptInterval) {
        this.nextAttemptInterval = nextAttemptInterval;
    }
}
