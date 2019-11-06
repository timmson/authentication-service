package ru.timmson.auth.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class AuthResponse<T extends AuthServiceResponse> {

    private Integer code;

    private T body;

    public AuthResponse(T body) {
        this.body = body;
    }

    public ResponseEntity<T> toResponseEntity() {
        return new ResponseEntity<>(body, HttpStatus.valueOf(code));
    }
}
