package review.cl.review_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import review.cl.review_service.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    boolean existsByUsuarioIdAndAnimeId(
            Long usuarioId,
            Long animeId
    );
    List<Review>
    findByAnimeId(
            Long animeId
    );

    List<Review>
    findByUsuarioId(
            Long usuarioId
    );

    List<Review>
    findByPuntuacion(
            Integer puntuacion
    );

}
