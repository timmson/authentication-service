package ru.agilix.auth.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthResponse<T extends AuthServiceResponse> {

    private Integer code;

    private T body;

    public AuthResponse(T body) {
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public ResponseEntity<T> toResponseEntity() {
        return new ResponseEntity<>(body, HttpStatus.valueOf(code));
    }
}
