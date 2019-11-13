package ru.agilix.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeneratorServiceShould {

    private GeneratorService generatorService;

    @BeforeEach
    void setUp() {
        generatorService = new GeneratorServiceImpl();
    }

    @Test
    void generateToken() {
        assertNotNull(generatorService.generateToken());
    }

    @Test
    void generateOTPWithSixNumbers() {
        assertThat(generatorService.generateOneTimePassword()).hasSize(6);
    }
}