package ru.timmson.message.sms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsDTO {

    private String number;

    private String body;

}
