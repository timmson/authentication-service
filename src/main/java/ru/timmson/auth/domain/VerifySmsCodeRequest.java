package ru.timmson.auth.domain;

import lombok.Data;

@Data
public class VerifySmsCodeRequest implements AuthServiceRequest {

    private String confirmationToken;

    private String smsCode;

}
