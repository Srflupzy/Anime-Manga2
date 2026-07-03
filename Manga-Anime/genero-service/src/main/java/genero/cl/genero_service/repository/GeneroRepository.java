package genero.cl.genero_service.repository;

import genero.cl.genero_service.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneroRepository extends JpaRepository<Genero,Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<Genero>
    findByNombreContainingIgnoreCase(
            String nombre
    );
}
