package favoritos.cl.favoritos_service.repository;

import favoritos.cl.favoritos_service.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritosRepository extends JpaRepository<Favorito,Long> {

    boolean existsByUsuarioIdAndAnimeId(
            Long usuarioId,
            Long animeId
    );

    List<Favorito>
    findByUsuarioId(
            Long usuarioId
    );

    List<Favorito>
    findByAnimeId(
            Long animeId
    );

    Long countByAnimeId(
            Long animeId
    );
}
