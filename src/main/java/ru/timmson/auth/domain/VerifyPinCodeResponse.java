package ru.timmson.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyPinCodeResponse implements AuthServiceResponse {

    private String errorMessage;

    private String confirmationToken;

    private Integer remainingAttempts;

    private Long nextAttemptInterval;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
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
