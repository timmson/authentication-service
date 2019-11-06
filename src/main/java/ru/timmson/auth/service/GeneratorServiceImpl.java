package ru.timmson.auth.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    private final Random random = new Random();

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateOneTimePassword() {
        return IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(random.nextInt(9)))
                .collect(Collectors.joining());
    }
}
