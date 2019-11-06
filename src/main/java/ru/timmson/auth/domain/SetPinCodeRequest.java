package ru.timmson.auth.domain;

import lombok.Data;

@Data
public class SetPinCodeRequest implements AuthServiceResponse {

    private String phoneNumber;

    private String pinCode;

}
