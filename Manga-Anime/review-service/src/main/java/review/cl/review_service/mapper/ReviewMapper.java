package review.cl.review_service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import review.cl.review_service.client.AnimeFeign;
import review.cl.review_service.client.UsuarioFeign;
import review.cl.review_service.dto.ReviewDTO;
import review.cl.review_service.model.Review;

@Component
public class ReviewMapper {
    @Autowired
    private UsuarioFeign usuarioFeign;
    @Autowired
    private AnimeFeign animeFeign;

    public ReviewDTO toDTO(
            Review review) {

        if (review == null)
            return null;

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComentario(review.getComentario());
        dto.setPuntuacion(review.getPuntuacion());
        try {
            dto.setUsuario(usuarioFeign.pedirUsuario(review.getUsuarioId()).getNombre());
        } catch (Exception e) {
            dto.setUsuario(null);
        }
        try {
            dto.setAnime(animeFeign.pedirAnime(review.getAnimeId()).getTitulo());
        } catch (Exception e) {
            dto.setAnime(null);
        }
        return dto;
    }
}
