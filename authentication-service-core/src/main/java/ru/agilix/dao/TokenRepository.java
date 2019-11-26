package ru.agilix.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.agilix.domain.Token;

import java.util.List;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    List<Token> findByConfirmationTokenAndStatus(String confirmationToken, String status);

    List<Token> findByPhoneNumberAndPinCodeAndStatus(String phoneNumber, String pinCode, String status);

    List<Token> findByPhoneNumber(String phoneNumber);
}
