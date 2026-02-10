package io.github.doi02.ena.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GAME", uniqueConstraints = {@UniqueConstraint(
        name = "ID_NAME_UNIQUE",
        columnNames={"GAME_ID", "GAME_NAME"}
)})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Game {

    @Id
    @Column(name="GAME_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name="GAME_NAME", nullable=false)
    private String name;

    private String category;

    private String imageUrl;

}
