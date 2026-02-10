package io.github.doi02.ena.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "POST")
public class Post {

    @Id
    @Column(name="POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 255, nullable = false)
    private String title;

    @NonNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "GAME_ID", nullable = false)
    private Game game;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_AT", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATED_AT", nullable = false)
    private Date updatedAt;
}
