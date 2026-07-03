package review.cl.review_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import review.cl.review_service.client.AnimeFeign;
import review.cl.review_service.client.UsuarioFeign;
import review.cl.review_service.dto.AnimeDTO;
import review.cl.review_service.dto.ReviewDTO;
import review.cl.review_service.dto.UsuarioDTO;
import review.cl.review_service.exception.ReviewNoEncontradaException;
import review.cl.review_service.model.Review;
import review.cl.review_service.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AnimeFeign animeFeign;
    private final UsuarioFeign usuarioFeign;

    public List<ReviewDTO>
    findAll() {
        return reviewRepository
                .findAll()
                .stream()
                .map(r -> {
                    String usuarioNombre;
                    String animeNombre;
                    try {
                        usuarioNombre =
                                usuarioFeign
                                        .pedirUsuario(
                                                r.getUsuarioId()
                                        )
                                        .getNombre();
                    } catch (Exception e) {
                        usuarioNombre = null;
                    }
                    try {

                        animeNombre =
                                animeFeign
                                        .pedirAnime(
                                                r.getAnimeId()
                                        )
                                        .getTitulo();

                    } catch (Exception e) {
                        animeNombre = null;
                    }
                    return new ReviewDTO(
                            r.getId(),
                            r.getComentario(),
                            r.getPuntuacion(),
                            usuarioNombre,
                            animeNombre
                    );

                })
                .toList();
    }

    public ReviewDTO
    findById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNoEncontradaException("Review no encontrada"));

        String usuarioNombre;
        String animeNombre;

        try {
            usuarioNombre = usuarioFeign.pedirUsuario(review.getUsuarioId()).getNombre();

        } catch (Exception e) {
            usuarioNombre = null;
        }

        try {
            animeNombre = animeFeign.pedirAnime(review.getAnimeId()).getTitulo();

        } catch (Exception e) {

            animeNombre = null;
        }
        return new ReviewDTO(
                review.getId(),
                review.getComentario(),
                review.getPuntuacion(),
                usuarioNombre,
                animeNombre
        );
    }

    public Review
    save(Review review) {
        review.setId(null);
        if (reviewRepository.existsByUsuarioIdAndAnimeId(review.getUsuarioId(), review.getAnimeId())) {
            throw new RuntimeException(
                    "El usuario ya realizó una review de este anime"
            );
        }

        AnimeDTO anime;

        try {
            anime =
                    animeFeign.pedirAnime(review.getAnimeId());
        } catch (Exception e) {
            throw new RuntimeException(
                    "El anime no existe"
            );
        }
        UsuarioDTO usuario;

        try {
            usuario = usuarioFeign.pedirUsuario(review.getUsuarioId());
        } catch (Exception e) {
            throw new RuntimeException(
                    "El usuario no existe"
            );
        }
        return reviewRepository
                .save(review);
    }

    public Review
    update(Long id, Review review) {
        Review reviewExistente = reviewRepository.findById(id).orElseThrow(() -> new ReviewNoEncontradaException("Review no encontrada"));

        reviewExistente.setComentario(review.getComentario());
        reviewExistente.setPuntuacion(review.getPuntuacion());
        reviewExistente.setUsuarioId(review.getUsuarioId());
        reviewExistente.setAnimeId(review.getAnimeId());
        return reviewRepository.save(reviewExistente);
    }

    public void
    delete(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNoEncontradaException("Review no encontrada"));
        reviewRepository.delete(review);
    }

    public List<Review>
    buscarPorAnime(Long animeId) {
        return reviewRepository.findByAnimeId(animeId);
    }
    public List<Review>
    buscarPorUsuario(Long usuarioId) {
        return reviewRepository.findByUsuarioId(usuarioId);
    }
    public List<Review>
    topReviews() {
        return reviewRepository.findByPuntuacion(10);
    }
    public Double
    promedioAnime(Long animeId) {
        List<Review> reviews = reviewRepository.findByAnimeId(animeId);
        return reviews.stream().mapToInt(Review::getPuntuacion).average().orElse(0);
    }
}