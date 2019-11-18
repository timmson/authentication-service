package ru.agilix.message.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import ru.agilix.message.sms.domain.SmsDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceShould {

    @InjectMocks
    private SmsServiceImpl smsService;

    @Mock
    private RestOperations restOperations;

    @Test
    void returnNonEmpty_whenSMSGateReplied() {
        SmsDTO smsDTO = new SmsDTO("22222", "Hello");
        when(restOperations.postForEntity(nullable(String.class), eq(smsDTO), ArgumentMatchers.<Class<String>>any())).thenReturn(ResponseEntity.ok(""));

        Optional<String> result = smsService.send(smsDTO);

        assertTrue(result.isPresent());
    }

    @Test
    void returnEmpty_whenSMSGateIsFailed() {
        SmsDTO smsDTO = new SmsDTO("22222", "Hello");
        when(restOperations.postForEntity(nullable(String.class), eq(smsDTO), ArgumentMatchers.<Class<String>>any())).thenReturn(ResponseEntity.status(500).body(""));

        Optional<String> result = smsService.send(smsDTO);

        assertFalse(result.isPresent());
    }
}