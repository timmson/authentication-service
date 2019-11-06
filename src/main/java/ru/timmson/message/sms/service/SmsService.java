package ru.timmson.message.sms.service;

import ru.timmson.message.sms.domain.SmsDTO;

import java.util.Optional;

public interface SmsService {

    Optional<String> send(SmsDTO smsDTO);
}
