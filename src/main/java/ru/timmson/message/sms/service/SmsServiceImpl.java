package ru.timmson.message.sms.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.timmson.Env;
import ru.timmson.message.sms.domain.SmsDTO;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SmsServiceImpl implements SmsService {

    private Env env;

    @PostConstruct
    public void postConstruct() {
        Twilio.init(env.getTwilioAuthSid(), env.getTwilioAuthToken());
    }

    @Override
    public Optional<String> send(SmsDTO smsDTO) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(smsDTO.getNumber()),
                    new PhoneNumber(env.getTwilioFrom()),
                    smsDTO.getBody()).create();
            return Optional.of(message.getSid());
        } catch (RuntimeException e) {
            log.error("Failed to send SMS to " + smsDTO.getNumber(), e);
        }
        return Optional.empty();

    }

}
