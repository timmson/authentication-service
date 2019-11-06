package ru.timmson.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cb_one_time_password")
public class OneTimePassword {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "ru.timmson.domain.UUIDGenerator")
    private String uuid;

    @Column
    private String phoneNumber;

    @Column
    private LocalDateTime createdDateTime;

    @Column
    private String password;

    @Column
    private String confirmationToken;

    @Column
    private String msgId;

}

