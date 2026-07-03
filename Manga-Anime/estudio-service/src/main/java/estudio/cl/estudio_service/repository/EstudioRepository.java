package estudio.cl.estudio_service.repository;

import estudio.cl.estudio_service.model.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudioRepository extends JpaRepository<Estudio,Long> {

    boolean existsByNombreIgnoreCase(
            String nombre
    );

    List<Estudio>
    findByNombreContainingIgnoreCase(
            String nombre
    );

    Optional<Estudio>
    findByNombreIgnoreCase(
            String nombre
    );
}
