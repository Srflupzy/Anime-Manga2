package anime.cl.anime_service.repository;

import anime.cl.anime_service.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AnimeRepository
        extends JpaRepository<Anime, Long> {

    boolean existsByTituloIgnoreCase(
            String titulo
    );

    List<Anime>
    findByTituloContainingIgnoreCase(
            String titulo
    );


}
