package ru.agilix.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agilix.dao.OneTimePasswordRepository;
import ru.agilix.domain.OneTimePassword;
import ru.agilix.message.sms.domain.SmsDTO;
import ru.agilix.message.sms.service.SmsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OneTimePasswordServiceShould {

    private final String confirmationToken = "1111-2222";

    private final String otp = "123456";

    private final String phoneNumber = "+79991234567";

    @InjectMocks
    private OneTimePasswordServiceImpl oneTimePasswordService;

    @Mock
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Mock
    private GeneratorService generatorService;

    @Mock
    private SmsService smsService;

    @Test
    void generateAndSendAndStoreOTP() {
        when(generatorService.generateOneTimePassword()).thenReturn(otp);
        when(smsService.send(any(SmsDTO.class))).thenReturn(Optional.of(""));

        Optional<String> result = oneTimePasswordService.sendTo(phoneNumber, confirmationToken);

        assertTrue(result.isPresent());
        verify(oneTimePasswordRepository, times(2)).save(any(OneTimePassword.class));
    }

    @Test
    void returnTrue_whenOTPAndConfirmationTokenIsValid() {
        when(oneTimePasswordRepository.findByPasswordAndConfirmationToken(eq(otp), eq(confirmationToken))).thenReturn(buildOneTimePasswords());

        boolean result = oneTimePasswordService.verify(otp, confirmationToken);

        assertTrue(result);
    }

    @Test
    void returnTrue_whenOTPAndConfirmationTokenIsNotValid() {
        when(oneTimePasswordRepository.findByPasswordAndConfirmationToken(eq(otp), eq(confirmationToken))).thenReturn(Collections.emptyList());

        boolean result = oneTimePasswordService.verify(otp, confirmationToken);

        assertFalse(result);
    }

    private List<OneTimePassword> buildOneTimePasswords() {
        OneTimePassword oneTimePasswordEntity = new OneTimePassword();
        oneTimePasswordEntity.setConfirmationToken(confirmationToken);
        oneTimePasswordEntity.setPassword(otp);
        return Collections.singletonList(oneTimePasswordEntity);
    }
}
