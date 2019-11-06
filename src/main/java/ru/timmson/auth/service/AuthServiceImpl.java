package ru.timmson.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.timmson.auth.domain.*;
import ru.timmson.dao.TokenRepository;
import ru.timmson.domain.Token;
import ru.timmson.domain.TokenStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.timmson.domain.TokenStatus.EXPIRED;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private GeneratorService generatorService;

    private TokenRepository tokenRepository;

    private OneTimePasswordService oneTimePasswordService;

    @Override
    public AuthResponse<CheckPhoneNumberResponse> checkPhoneNumber(CheckPhoneNumberRequest request) {
        final var tokens = tokenRepository.findByPhoneNumber(request.getPhoneNumber()).stream().filter(t -> !t.getStatus().equals(EXPIRED)).collect(Collectors.toList());
        final var response = new AuthResponse<>(new CheckPhoneNumberResponse());

        response.setCode(200);
        response.getBody().setAvailable(false);

        if (tokens.size() == 1) {
            final var status = tokens.get(0).getStatus();
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
        final var phoneNumber = request.getPhoneNumber();

        final var response = new AuthResponse<>(new SetPinCodeResponse());

        var confirmationToken = generatorService.generateToken();
        var msgId = oneTimePasswordService.sendTo(phoneNumber, confirmationToken);
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
        final var isValid = oneTimePasswordService.verify(request.getSmsCode(), request.getConfirmationToken());

        final var response = new AuthResponse<>(new VerifySmsCodeResponse());

        if (isValid) {
            List<Token> tokens = tokenRepository.findByConfirmationTokenAndStatus(request.getConfirmationToken(), TokenStatus.WAITING_FOR_APPROVE);
            if (tokens.size() == 1) {
                response.setCode(200);
                var token = tokens.get(0);
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
        final var tokens = tokenRepository.findByPhoneNumberAndPinCodeAndStatus(request.getPhoneNumber(), request.getPinCode(), TokenStatus.ACTIVE);

        final var response = new AuthResponse<>(new VerifyPinCodeResponse());

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
        final var tokens = tokenRepository.findByPhoneNumber(request.getPhoneNumber());
        tokens.forEach(t -> t.setStatus(EXPIRED));
        tokenRepository.saveAll(tokens);

        final var token = new Token();
        token.setPhoneNumber(request.getPhoneNumber());
        token.setPinCode(request.getPinCode());
        token.setConfirmationToken(confirmationToken);
        token.setCreatedDateTime(LocalDateTime.now());
        token.setStatus(TokenStatus.WAITING_FOR_APPROVE);
        tokenRepository.save(token);
    }
}
