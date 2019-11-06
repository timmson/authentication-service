package ru.timmson.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckPhoneNumberResponse implements AuthServiceResponse {

    private Boolean available;

    private String status;

    private Long timeToUnblocked;

}
