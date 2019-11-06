package ru.timmson.auth.domain;

import lombok.Data;

@Data
public class CheckPhoneNumberRequest implements AuthServiceRequest {

    private String phoneNumber;

}
