package ru.timmson.auth.service;

import ru.timmson.auth.domain.*;

public interface AuthService {

    AuthResponse<SetPinCodeResponse> setPinCode(SetPinCodeRequest request);

    AuthResponse<VerifySmsCodeResponse> verifySmsCode(VerifySmsCodeRequest request);

    AuthResponse<VerifyPinCodeResponse> verifyPinCode(VerifyPinCodeRequest request);

    AuthResponse<CheckPhoneNumberResponse> checkPhoneNumber(CheckPhoneNumberRequest request);
}
