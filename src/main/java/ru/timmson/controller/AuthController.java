package ru.timmson.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.timmson.auth.domain.*;
import ru.timmson.auth.service.AuthService;

@RestController
@RequestMapping("/v1/auth/")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Запрос на отправку номера телефона")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "403", description = "Error")
    @PostMapping(value = "/checkPhoneNumber", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckPhoneNumberResponse> checkPhoneNumber(@RequestBody CheckPhoneNumberRequest request) {
        return authService.checkPhoneNumber(request).toResponseEntity();
    }

    @Operation(summary = "Запрос на создание пинкода")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "Error (Ошибка на стороне сервера при отправке SMS)")
    @PostMapping(value = "/setPinCode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SetPinCodeResponse> setPinCode(@RequestBody SetPinCodeRequest request) {
        return authService.setPinCode(request).toResponseEntity();
    }

    @Operation(summary = "Запрос на проверку ранее созданного пинкода")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "403", description = "Error")
    @PostMapping(value = "/verifyPinCode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VerifyPinCodeResponse> verifyPinCode(@RequestBody VerifyPinCodeRequest request) {
        return authService.verifyPinCode(request).toResponseEntity();
    }

    @Operation(summary = "Запрос на проверку кода из смс")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "403", description = "Error")
    @PostMapping(value = "/verifySmsCode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VerifySmsCodeResponse> verifySmsCode(@RequestBody VerifySmsCodeRequest request) {
        return authService.verifySmsCode(request).toResponseEntity();
    }

}
