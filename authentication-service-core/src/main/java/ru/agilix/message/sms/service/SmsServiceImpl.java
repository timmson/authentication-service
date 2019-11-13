package ru.agilix.message.sms.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;
import ru.agilix.Env;
import ru.agilix.message.sms.domain.SmsDTO;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class SmsServiceImpl implements SmsService {

    private Env env;

    public SmsServiceImpl(Env env) {
        this.env = env;
    }

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
            System.err.println("Failed to send SMS to " + smsDTO.getNumber());
        }
        return Optional.empty();

    }

}
