package ru.timmson.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cb_token")
public class Token {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "ru.timmson.domain.UUIDGenerator")
    private String uuid;

    @Column
    private String phoneNumber;

    @Column
    private String pinCode;

    @Column
    private String status;

    @Column
    private String confirmationToken;

    @Column
    private LocalDateTime createdDateTime;

}
