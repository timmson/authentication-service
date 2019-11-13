package ru.agilix.auth.service;

import org.springframework.stereotype.Service;
import ru.agilix.auth.domain.*;
import ru.agilix.dao.TokenRepository;
import ru.agilix.domain.Token;
import ru.agilix.domain.TokenStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.agilix.domain.TokenStatus.EXPIRED;

@Service
public class AuthServiceImpl implements AuthService {

    private GeneratorService generatorService;

    private TokenRepository tokenRepository;

    private OneTimePasswordService oneTimePasswordService;

    public AuthServiceImpl(GeneratorService generatorService, TokenRepository tokenRepository, OneTimePasswordService oneTimePasswordService) {
        this.generatorService = generatorService;
        this.tokenRepository = tokenRepository;
        this.oneTimePasswordService = oneTimePasswordService;
    }

    @Override
    public AuthResponse<CheckPhoneNumberResponse> checkPhoneNumber(CheckPhoneNumberRequest request) {
        List<Token> tokens = tokenRepository.findByPhoneNumber(request.getPhoneNumber()).stream().filter(t -> !t.getStatus().equals(EXPIRED)).collect(Collectors.toList());
        AuthResponse<CheckPhoneNumberResponse> response = new AuthResponse<>(new CheckPhoneNumberResponse());

        response.setCode(200);
        response.getBody().setAvailable(false);

        if (tokens.size() == 1) {
            String status = tokens.get(0).getStatus();
            response.getBody().setStatus(status);

            if (status.equals(TokenStatus.ACTIVE)) {
                response.getBody().setAvailable(true);
            } else if (status.equals(TokenStatus.BLOCKED)) {
                response.setCode(403);
                response.getBody().setAvailable(null);
            }

        }

        return response;
    }

    @Override
    public AuthResponse<SetPinCodeResponse> setPinCode(SetPinCodeRequest request) {
        String phoneNumber = request.getPhoneNumber();

        AuthResponse<SetPinCodeResponse> response = new AuthResponse<>(new SetPinCodeResponse());

        String confirmationToken = generatorService.generateToken();
        Optional<String> msgId = oneTimePasswordService.sendTo(phoneNumber, confirmationToken);
        if (msgId.isPresent()) {
            saveToken(request, confirmationToken);
            response.setCode(200);
            response.getBody().setConfirmationToken(confirmationToken);
        } else {
            response.setCode(500);
            response.getBody().setErrorMessage("SMS was not send");
        }

        return response;
    }

    @Override
    public AuthResponse<VerifySmsCodeResponse> verifySmsCode(VerifySmsCodeRequest request) {
        boolean isValid = oneTimePasswordService.verify(request.getSmsCode(), request.getConfirmationToken());

        AuthResponse<VerifySmsCodeResponse> response = new AuthResponse<>(new VerifySmsCodeResponse());

        if (isValid) {
            List<Token> tokens = tokenRepository.findByConfirmationTokenAndStatus(request.getConfirmationToken(), TokenStatus.WAITING_FOR_APPROVE);
            if (tokens.size() == 1) {
                response.setCode(200);
                Token token = tokens.get(0);
                token.setStatus(TokenStatus.ACTIVE);
                tokenRepository.save(token);
            } else {
                response.setCode(403);
                response.getBody().setErrorMessage("Token is not valid");
            }
        } else {
            response.setCode(403);
            response.getBody().setErrorMessage("SmsCode is not valid");
        }

        return response;
    }

    @Override
    public AuthResponse<VerifyPinCodeResponse> verifyPinCode(VerifyPinCodeRequest request) {
        List<Token> tokens = tokenRepository.findByPhoneNumberAndPinCodeAndStatus(request.getPhoneNumber(), request.getPinCode(), TokenStatus.ACTIVE);

        AuthResponse<VerifyPinCodeResponse> response = new AuthResponse<>(new VerifyPinCodeResponse());

        if (tokens.size() == 1) {
            response.setCode(200);
            response.getBody().setConfirmationToken(tokens.get(0).getConfirmationToken());
        } else {
            response.setCode(403);
            response.getBody().setErrorMessage("PinCode is not valid");
        }

        return response;
    }

    private void saveToken(SetPinCodeRequest request, String confirmationToken) {
        List<Token> tokens = tokenRepository.findByPhoneNumber(request.getPhoneNumber());
        tokens.forEach(t -> t.setStatus(EXPIRED));
        tokenRepository.saveAll(tokens);

        Token token = new Token();
        token.setPhoneNumber(request.getPhoneNumber());
        token.setPinCode(request.getPinCode());
        token.setConfirmationToken(confirmationToken);
        token.setCreatedDateTime(LocalDateTime.now());
        token.setStatus(TokenStatus.WAITING_FOR_APPROVE);
        tokenRepository.save(token);
    }
}
