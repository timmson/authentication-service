package ru.agilix.message.sms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.agilix.message.sms.domain.SmsDTO;

import java.util.Optional;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${sms.gate.url}")
    private String smsGateUrl;

    @Override
    public Optional<String> send(SmsDTO smsDTO) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(smsGateUrl, smsDTO, String.class);
            return Optional.ofNullable(response.getBody());
        } catch (RuntimeException e) {
            System.err.println("Failed to send SMS to " + smsDTO.getNumber());
        }
        return Optional.empty();

    }

}
