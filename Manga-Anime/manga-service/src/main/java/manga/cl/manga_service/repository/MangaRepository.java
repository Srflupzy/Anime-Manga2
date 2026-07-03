package manga.cl.manga_service.repository;

import manga.cl.manga_service.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MangaRepository extends JpaRepository<Manga,Long> {

    boolean existsByTituloIgnoreCase(
            String titulo
    );

    List<Manga>
    findByTituloContainingIgnoreCase(
            String titulo
    );
}
