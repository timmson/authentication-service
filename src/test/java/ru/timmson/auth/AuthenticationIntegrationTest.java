package ru.timmson.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.timmson.auth.service.GeneratorService;
import ru.timmson.dao.OneTimePasswordRepository;
import ru.timmson.dao.TokenRepository;
import ru.timmson.message.sms.domain.SmsDTO;
import ru.timmson.message.sms.service.SmsService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @MockBean
    private GeneratorService generatorService;

    @MockBean
    private SmsService smsService;


    @BeforeEach
    void setUp() {
        tokenRepository.deleteAll();
        oneTimePasswordRepository.deleteAll();
    }

    @Test
    void userSetsPinCode() throws Exception {
        final var phoneNumber = "+79991234567";
        final var pinCode = "1111";
        final var smsCode = "123456";
        final var confirmationToken = "2222-3333";

        //Check phone
        performPostRequest(
                "/v1/auth/checkPhoneNumber", "{\"phoneNumber\":\"" + phoneNumber + "\"}",
                200, "{\"available\": false\n}");

        //Verify PinCode, expects 403 (PinCode is not set)
        performPostRequest(
                "/v1/auth/verifyPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                403, "{\"errorMessage\": \"PinCode is not valid\"}");

        //Set PinCode and send SmsCode
        when(generatorService.generateOneTimePassword()).thenReturn(smsCode);
        when(smsService.send(any(SmsDTO.class))).thenReturn(Optional.of(""));
        when(generatorService.generateToken()).thenReturn(confirmationToken);
        performPostRequest(
                "/v1/auth/setPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                200, "{\"confirmationToken\": \"" + confirmationToken + "\"}"
        );

        //Verify SmsCode in DB
        var oneTimePassword = oneTimePasswordRepository.findAll().iterator().next();
        assertNotNull(oneTimePassword.getMsgId());
        assertTrue(LocalDateTime.now().isAfter(oneTimePassword.getCreatedDateTime()));

        //Verify PinCode, expects 403 (PinCode is not approve)
        performPostRequest(
                "/v1/auth/verifyPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                403, "{\"errorMessage\": \"PinCode is not valid\"}");

        //Verify SmsCode, expects 403 (SmsCode is wrong)
        performPostRequest(
                "/v1/auth/verifySmsCode", "{\"smsCode\": \"" + smsCode.concat("0") + "\" , \"confirmationToken\": \"" + confirmationToken + "\"}",
                403, "{}"
        );

        //Verify SmsCode
        performPostRequest(
                "/v1/auth/verifySmsCode", "{\"smsCode\": \"" + smsCode + "\" , \"confirmationToken\": \"" + confirmationToken + "\"}",
                200, "{}"
        );

        //Verify PinCode
        performPostRequest(
                "/v1/auth/verifyPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                200, "{\"confirmationToken\": \"" + confirmationToken + "\"}");

        //Verify token in DB
        var token = tokenRepository.findAll().iterator().next();
        assertTrue(LocalDateTime.now().isAfter(token.getCreatedDateTime()));

        //Check phone
        performPostRequest(
                "/v1/auth/checkPhoneNumber", "{\"phoneNumber\":\"" + phoneNumber + "\"}",
                200, "{\"available\": true\n}");

    }

    private void performPostRequest(String url, String requestBody, Integer statusCode, String responseBody) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestBody));
        resultActions.andExpect(status().is(statusCode)).andExpect(content().json(responseBody));
    }


}
