package ru.timmson.domain;

public interface TokenStatus {

    String WAITING_FOR_APPROVE = "WAITING_FOR_APPROVE";

    String ACTIVE = "ACTIVE";

    String EXPIRED = "EXPIRED";

    String BLOCKED = "BLOCKED";
}
