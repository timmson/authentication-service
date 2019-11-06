package ru.timmson.auth.service;

public interface GeneratorService {

    String generateToken();

    String generateOneTimePassword();

}
