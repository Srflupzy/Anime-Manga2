package episodio.cl.episodio_service.repository;

import episodio.cl.episodio_service.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodioRepository extends JpaRepository<Episodio,Long> {


    boolean existsByTituloIgnoreCaseAndTemporadaId(
            String titulo,
            Long temporadaId
    );

    List<Episodio>
    findByTituloContainingIgnoreCase(
            String titulo
    );

    List<Episodio>
    findByTemporadaId(
            Long temporadaId
    );

    Optional<Episodio>
    findTopByOrderByDuracionDesc();
}
