package ru.agilix.auth.test;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.agilix.auth.domain.CheckPhoneNumberRequest;
import ru.agilix.auth.domain.CheckPhoneNumberResponse;
import ru.agilix.auth.domain.SetPinCodeRequest;
import ru.agilix.auth.domain.SetPinCodeResponse;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class AuthenticationStepDefinitions {

    private String url = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();

    private String phoneNumber;
    private Boolean isUserExist;
    private boolean isResponseOk;

    @Дано("Номер телефона пользователя {string}")
    public void userPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Когда("Проверяем наличие пользователя")
    public void checkIfUserExists() {
        final CheckPhoneNumberRequest request = new CheckPhoneNumberRequest();
        request.setPhoneNumber(phoneNumber);

        CheckPhoneNumberResponse response = restTemplate.postForObject(url + "/v1/auth/checkPhoneNumber", request, CheckPhoneNumberResponse.class);
        this.isUserExist = Optional.ofNullable(response).orElse(new CheckPhoneNumberResponse()).getAvailable();
    }

    @Тогда("Пользователь {string} найден")
    public void isUserExist(String yesNo) {
        assertEquals(yesNo.equalsIgnoreCase(""), isUserExist);
    }

    @Когда("Устанавливае PIN-кода {string}")
    public void userSetsPinCode(String pinCode) {
        SetPinCodeRequest request = new SetPinCodeRequest();
        request.setPhoneNumber(phoneNumber);
        request.setPinCode(pinCode);

        ResponseEntity<SetPinCodeResponse> response = restTemplate.postForEntity(url + "/v1/auth/setPinCode", request, SetPinCodeResponse.class);
        isResponseOk = response.getStatusCode().is2xxSuccessful();
    }

    @Тогда("Установка {string} проходит успешно")
    public void isResponseOk(String yesNo) {
        assertEquals(yesNo.equalsIgnoreCase(""), isResponseOk);
    }
}
