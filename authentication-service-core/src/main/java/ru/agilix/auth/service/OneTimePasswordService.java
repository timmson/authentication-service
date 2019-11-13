package ru.agilix.auth.service;

import java.util.Optional;

public interface OneTimePasswordService {

    Optional<String> sendTo(String phoneNumber, String confirmationToken);

    boolean verify(String password, String confirmationToken);
}
