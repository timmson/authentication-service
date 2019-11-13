package ru.agilix.auth.service;

import org.springframework.stereotype.Service;
import ru.agilix.dao.OneTimePasswordRepository;
import ru.agilix.domain.OneTimePassword;
import ru.agilix.message.sms.domain.SmsDTO;
import ru.agilix.message.sms.service.SmsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OneTimePasswordServiceImpl implements OneTimePasswordService {

    private GeneratorService generatorService;

    private OneTimePasswordRepository oneTimePasswordRepository;

    private SmsService smsService;

    public OneTimePasswordServiceImpl(GeneratorService generatorService, OneTimePasswordRepository oneTimePasswordRepository, SmsService smsService) {
        this.generatorService = generatorService;
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.smsService = smsService;
    }

    @Override
    public Optional<String> sendTo(String phoneNumber, String confirmationToken) {
        String otp = generatorService.generateOneTimePassword();
        OneTimePassword oneTimePassword = new OneTimePassword();
        oneTimePassword.setPhoneNumber(phoneNumber);
        oneTimePassword.setPassword(otp);
        oneTimePassword.setCreatedDateTime(LocalDateTime.now());
        oneTimePassword.setConfirmationToken(confirmationToken);
        oneTimePasswordRepository.save(oneTimePassword);

        Optional<String> msgId = smsService.send(new SmsDTO(phoneNumber, "Your OTP is: " + otp));
        if (msgId.isPresent()) {
            oneTimePassword.setMsgId(msgId.get());
            oneTimePasswordRepository.save(oneTimePassword);
        }
        return msgId;
    }

    @Override
    public boolean verify(String password, String confirmationToken) {
        List<OneTimePassword> oneTimePasswords = oneTimePasswordRepository.findByPasswordAndConfirmationToken(password, confirmationToken);
        return oneTimePasswords.size() == 1;
    }
}
