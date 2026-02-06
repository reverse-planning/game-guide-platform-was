package io.github.doi02.ena.repsository;

import io.github.doi02.ena.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>{

}
