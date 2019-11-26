package ru.agilix.message.sms.service;

import ru.agilix.message.sms.domain.SmsDTO;

import java.util.Optional;

public interface SmsService {

    Optional<String> send(SmsDTO smsDTO);
}
