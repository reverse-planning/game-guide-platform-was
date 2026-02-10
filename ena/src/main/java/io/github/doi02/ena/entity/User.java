package io.github.doi02.ena.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name ="USER", uniqueConstraints = {@UniqueConstraint(
        name = "ID_NICKNAME_UNIQUE",
        columnNames = {"USER_ID", "NICKNAME"} )})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NICKNAME", nullable = false, length = 10)
    private String nickname;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_AT", updatable = false)
    private Date CreatedAt;

    private String socialLoginId;
}
