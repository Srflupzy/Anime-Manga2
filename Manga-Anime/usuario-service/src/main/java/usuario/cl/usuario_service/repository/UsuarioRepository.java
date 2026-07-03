package usuario.cl.usuario_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usuario.cl.usuario_service.Rol;
import usuario.cl.usuario_service.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    boolean existsByEmailIgnoreCase(
            String email
    );

    List<Usuario>
    findByNombreContainingIgnoreCase(
            String nombre
    );

    Optional<Usuario>
    findByEmailIgnoreCase(
            String email
    );

    List<Usuario>
    findByRol(
            Rol rol
    );

}
