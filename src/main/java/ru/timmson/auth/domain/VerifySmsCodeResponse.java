package ru.timmson.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
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
}
