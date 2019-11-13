package ru.agilix.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.agilix.domain.OneTimePassword;

import java.util.List;

@Repository
public interface OneTimePasswordRepository extends CrudRepository<OneTimePassword, String> {

    List<OneTimePassword> findByPasswordAndConfirmationToken(String confirmationToken, String password);
}
