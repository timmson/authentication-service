package ru.timmson.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.timmson.dao.OneTimePasswordRepository;
import ru.timmson.domain.OneTimePassword;
import ru.timmson.message.sms.domain.SmsDTO;
import ru.timmson.message.sms.service.SmsService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OneTimePasswordServiceImpl implements OneTimePasswordService {

    private GeneratorService generatorService;

    private OneTimePasswordRepository oneTimePasswordRepository;

    private SmsService smsService;

    @Override
    public Optional<String> sendTo(String phoneNumber, String confirmationToken) {
        var otp = generatorService.generateOneTimePassword();
        OneTimePassword oneTimePassword = new OneTimePassword();
        oneTimePassword.setPhoneNumber(phoneNumber);
        oneTimePassword.setPassword(otp);
        oneTimePassword.setCreatedDateTime(LocalDateTime.now());
        oneTimePassword.setConfirmationToken(confirmationToken);
        oneTimePasswordRepository.save(oneTimePassword);

        var msgId = smsService.send(new SmsDTO(phoneNumber, "Your OTP is: " + otp));
        if (msgId.isPresent()) {
            oneTimePassword.setMsgId(msgId.get());
            oneTimePasswordRepository.save(oneTimePassword);
        }
        return msgId;
    }

    @Override
    public boolean verify(String password, String confirmationToken) {
        var oneTimePasswords = oneTimePasswordRepository.findByPasswordAndConfirmationToken(password, confirmationToken);
        return oneTimePasswords.size() == 1;
    }
}
