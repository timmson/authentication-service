package ru.timmson.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyPinCodeResponse implements AuthServiceResponse {

    private String errorMessage;

    private String confirmationToken;

    private Integer remainingAttempts;

    private Long nextAttemptInterval;

}
