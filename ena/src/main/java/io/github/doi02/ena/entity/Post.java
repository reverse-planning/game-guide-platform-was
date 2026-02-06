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
    @JoinColumn(name = "USER", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
