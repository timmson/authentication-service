package ru.timmson.auth.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.timmson.auth.domain.*;
import ru.timmson.dao.TokenRepository;
import ru.timmson.domain.Token;
import ru.timmson.domain.TokenStatus;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceShould {

    private final String phoneNumber = "+79991234567";

    private final String pinCode = "1111";

    private final String oneTimePassword = "123456";

    private final String confirmationToken = "2222-33333";


    @InjectMocks
    private AuthServiceImpl service;

    @Mock
    private GeneratorService generatorService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private OneTimePasswordService oneTimePasswordService;

    @Test
    void return200_whenUserIsNotExist() {
        CheckPhoneNumberRequest request = new CheckPhoneNumberRequest();
        request.setPhoneNumber(phoneNumber);
        when(tokenRepository.findByPhoneNumber(eq(phoneNumber))).thenReturn(Collections.emptyList());

        AuthResponse<CheckPhoneNumberResponse> result = service.checkPhoneNumber(request);

        Assertions.assertEquals(200, result.getCode());
        Assertions.assertFalse(result.getBody().getAvailable());
    }

    @Test
    void return200_whenUserIsExist() {
        CheckPhoneNumberRequest request = new CheckPhoneNumberRequest();
        request.setPhoneNumber(phoneNumber);

        Token token = new Token();
        token.setStatus(TokenStatus.ACTIVE);
        when(tokenRepository.findByPhoneNumber(eq(phoneNumber))).thenReturn(Collections.singletonList(token));

        AuthResponse<CheckPhoneNumberResponse> result = service.checkPhoneNumber(request);

        Assertions.assertEquals(200, result.getCode());
        Assertions.assertTrue(result.getBody().getAvailable());
        Assertions.assertEquals(TokenStatus.ACTIVE, result.getBody().getStatus());
    }

    @Test
    void return403_whenUserIsExist() {
        CheckPhoneNumberRequest request = new CheckPhoneNumberRequest();
        request.setPhoneNumber(phoneNumber);

        Token token = new Token();
        token.setStatus(TokenStatus.BLOCKED);
        when(tokenRepository.findByPhoneNumber(eq(phoneNumber))).thenReturn(Collections.singletonList(token));

        AuthResponse<CheckPhoneNumberResponse> result = service.checkPhoneNumber(request);

        Assertions.assertEquals(403, result.getCode());
        assertNull(result.getBody().getAvailable());
        Assertions.assertEquals(TokenStatus.BLOCKED, result.getBody().getStatus());
    }

    @Test
    void return200AndConfirmationToken_whenPinCodeSavedAndSmsSent() {
        SetPinCodeRequest request = buildSetPinCodeRequest();
        when(generatorService.generateToken()).thenReturn(confirmationToken);
        when(oneTimePasswordService.sendTo(eq(phoneNumber), eq(confirmationToken))).thenReturn(Optional.of(""));

        AuthResponse<SetPinCodeResponse> result = service.setPinCode(request);

        Assertions.assertEquals(200, result.getCode());
        Assertions.assertEquals(confirmationToken, result.getBody().getConfirmationToken());
        assertNull(result.getBody().getErrorMessage());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void return500_whenSmsSendingFailed() {
        SetPinCodeRequest request = buildSetPinCodeRequest();
        when(generatorService.generateToken()).thenReturn(confirmationToken);
        when(oneTimePasswordService.sendTo(eq(phoneNumber), eq(confirmationToken))).thenReturn(Optional.empty());

        AuthResponse<SetPinCodeResponse> result = service.setPinCode(request);

        Assertions.assertEquals(500, result.getCode());
        assertNotNull(result.getBody().getErrorMessage());
        assertNull(result.getBody().getConfirmationToken());
        verify(generatorService, times(1)).generateToken();
        verify(tokenRepository, never()).save(any(Token.class));
    }

    /**
     * TODO
     * Add more conditions
     */
    @Test
    void return200_whenSmsCodeIsRight() {
        VerifySmsCodeRequest request = buildVerifySmsCodeRequest();
        Token token = new Token();
        when(oneTimePasswordService.verify(eq(oneTimePassword), eq(confirmationToken))).thenReturn(true);
        when(tokenRepository.findByConfirmationTokenAndStatus(eq(confirmationToken), eq(TokenStatus.WAITING_FOR_APPROVE))).thenReturn(Collections.singletonList(token));

        AuthResponse<VerifySmsCodeResponse> result = service.verifySmsCode(request);

        Assertions.assertEquals(200, result.getCode());
        assertNull(result.getBody().getErrorMessage());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void return403_whenTokenIsMissing() {
        VerifySmsCodeRequest request = buildVerifySmsCodeRequest();
        when(oneTimePasswordService.verify(eq(oneTimePassword), eq(confirmationToken))).thenReturn(true);
        when(tokenRepository.findByConfirmationTokenAndStatus(eq(confirmationToken), eq(TokenStatus.WAITING_FOR_APPROVE))).thenReturn(Collections.emptyList());

        AuthResponse<VerifySmsCodeResponse> result = service.verifySmsCode(request);

        Assertions.assertEquals(403, result.getCode());
        assertNotNull(result.getBody().getErrorMessage());
        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void return403_whenSmsCodeIsWrong() {
        VerifySmsCodeRequest request = buildVerifySmsCodeRequest();
        when(oneTimePasswordService.verify(eq(oneTimePassword), eq(confirmationToken))).thenReturn(false);

        AuthResponse<VerifySmsCodeResponse> result = service.verifySmsCode(request);

        Assertions.assertEquals(403, result.getCode());
        assertNotNull(result.getBody().getErrorMessage());
        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void return200_whenPinCodeIsValid() {
        VerifyPinCodeRequest request = new VerifyPinCodeRequest();
        request.setPhoneNumber(phoneNumber);
        request.setPinCode(pinCode);

        Token token = new Token();
        token.setConfirmationToken(confirmationToken);
        when(tokenRepository.findByPhoneNumberAndPinCodeAndStatus(eq(phoneNumber), eq(pinCode), eq(TokenStatus.ACTIVE))).thenReturn(Collections.singletonList(token));

        AuthResponse<VerifyPinCodeResponse> result = service.verifyPinCode(request);

        Assertions.assertEquals(200, result.getCode());
        Assertions.assertEquals(confirmationToken, result.getBody().getConfirmationToken());
        assertNull(result.getBody().getErrorMessage());

    }

    @Test
    void return403_whenPinCodeIsValid() {
        VerifyPinCodeRequest request = new VerifyPinCodeRequest();
        request.setPhoneNumber(phoneNumber);
        request.setPinCode(pinCode);
        when(tokenRepository.findByPhoneNumberAndPinCodeAndStatus(eq(phoneNumber), eq(pinCode), eq(TokenStatus.ACTIVE))).thenReturn(Collections.emptyList());

        AuthResponse<VerifyPinCodeResponse> result = service.verifyPinCode(request);

        Assertions.assertEquals(403, result.getCode());
        assertNull(result.getBody().getConfirmationToken());
        assertNotNull(result.getBody().getErrorMessage());
    }

    private VerifySmsCodeRequest buildVerifySmsCodeRequest() {
        VerifySmsCodeRequest request = new VerifySmsCodeRequest();
        request.setSmsCode(oneTimePassword);
        request.setConfirmationToken(confirmationToken);
        return request;
    }

    private SetPinCodeRequest buildSetPinCodeRequest() {
        SetPinCodeRequest request = new SetPinCodeRequest();
        request.setPhoneNumber(phoneNumber);
        request.setPinCode(pinCode);
        return request;
    }
}
