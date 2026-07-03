package temporada.cl.temporada_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import temporada.cl.temporada_service.model.Temporada;

import java.util.List;
import java.util.Optional;

public interface TemporadaRepository extends JpaRepository<Temporada,Long> {

    boolean existsByNumeroAndAnimeId(
            Integer numero,
            Long animeId
    );

    List<Temporada>
    findByNumero(
            Integer numero
    );

    List<Temporada>
    findByAnimeId(
            Long animeId
    );

    Optional<Temporada>
    findByAnimeIdAndNumero(
            Long animeId,
            Integer numero
    );
}
