package ru.timmson.auth.domain;

import lombok.Data;

@Data
public class AuthRequest<T extends AuthServiceRequest> {
    private T request;

}
