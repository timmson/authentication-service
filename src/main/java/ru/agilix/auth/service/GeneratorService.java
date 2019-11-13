package ru.agilix.auth.service;

public interface GeneratorService {

    String generateToken();

    String generateOneTimePassword();

}
