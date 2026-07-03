package personaje.cl.personaje_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personaje.cl.personaje_service.model.Personaje;

import java.util.List;

public interface PersonajeRepository extends JpaRepository<Personaje,Long> {

    boolean existsByNombreIgnoreCaseAndAnimeId(
            String nombre,
            Long animeId
    );

    List<Personaje>
    findByNombreContainingIgnoreCase(
            String nombre
    );
}
