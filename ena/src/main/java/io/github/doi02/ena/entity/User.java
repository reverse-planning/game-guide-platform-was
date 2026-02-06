package io.github.doi02.ena.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name ="USER", uniqueConstraints = {@UniqueConstraint(
        name = "ID_NICKNAME_UNIQUE",
        columnNames = {"ID", "NICKNAME"} )})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedAt;

    private String socialLoginId;
}
