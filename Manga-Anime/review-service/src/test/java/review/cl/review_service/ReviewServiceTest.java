package review.cl.review_service;

import review.cl.review_service.client.AnimeFeign;
import review.cl.review_service.client.UsuarioFeign;
import review.cl.review_service.dto.AnimeDTO;
import review.cl.review_service.dto.ReviewDTO;
import review.cl.review_service.dto.UsuarioDTO;
import review.cl.review_service.exception.ReviewNoEncontradaException;
import review.cl.review_service.model.Review;
import review.cl.review_service.repository.ReviewRepository;
import review.cl.review_service.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para ReviewService")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AnimeFeign animeFeign;

    @Mock
    private UsuarioFeign usuarioFeign;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private AnimeDTO animeDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId(1L);
        review.setComentario("Muy buen anime");
        review.setPuntuacion(10);
        review.setUsuarioId(1L);
        review.setAnimeId(1L);

        animeDTO = new AnimeDTO();
        animeDTO.setId(1L);
        animeDTO.setTitulo("Demon Slayer");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Fernando");
        usuarioDTO.setCorreo("fernando@gmail.com");
    }

    @Test
    @DisplayName("Debe listar todas las reviews con usuario y anime correctamente")
    void findAll_deberiaRetornarListaDeReviewDTO() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));
        when(usuarioFeign.pedirUsuario(1L)).thenReturn(usuarioDTO);
        when(animeFeign.pedirAnime(1L)).thenReturn(animeDTO);

        List<ReviewDTO> resultado = reviewService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Muy buen anime", resultado.get(0).getComentario());
        assertEquals("Fernando", resultado.get(0).getUsuario());
        assertEquals("Demon Slayer", resultado.get(0).getAnime());

        verify(reviewRepository).findAll();
        verify(usuarioFeign).pedirUsuario(1L);
        verify(animeFeign).pedirAnime(1L);
    }

    @Test
    @DisplayName("Debe buscar review por ID cuando existe")
    void findById_cuandoExiste_deberiaRetornarReviewDTO() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(usuarioFeign.pedirUsuario(1L)).thenReturn(usuarioDTO);
        when(animeFeign.pedirAnime(1L)).thenReturn(animeDTO);

        ReviewDTO resultado = reviewService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Muy buen anime", resultado.getComentario());
        assertEquals("Fernando", resultado.getUsuario());
        assertEquals("Demon Slayer", resultado.getAnime());

        verify(reviewRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ReviewNoEncontradaException cuando no existe")
    void findById_cuandoNoExiste_deberiaLanzarException() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        ReviewNoEncontradaException exception = assertThrows(
                ReviewNoEncontradaException.class,
                () -> reviewService.findById(99L)
        );

        assertEquals("Review no encontrada", exception.getMessage());
        verify(reviewRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar una review correctamente validando usuario y anime")
    void save_deberiaGuardarYRetornarReview() {
        when(reviewRepository.existsByUsuarioIdAndAnimeId(1L, 1L)).thenReturn(false);
        when(animeFeign.pedirAnime(1L)).thenReturn(animeDTO);
        when(usuarioFeign.pedirUsuario(1L)).thenReturn(usuarioDTO);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review resultado = reviewService.save(review);

        assertNotNull(resultado);
        assertEquals("Muy buen anime", resultado.getComentario());

        verify(reviewRepository).existsByUsuarioIdAndAnimeId(1L, 1L);
        verify(animeFeign).pedirAnime(1L);
        verify(usuarioFeign).pedirUsuario(1L);
        verify(reviewRepository).save(review);
    }

    @Test
    @DisplayName("Debe buscar reviews por anime correctamente")
    void buscarPorAnime_deberiaRetornarLista() {
        when(reviewRepository.findByAnimeId(1L)).thenReturn(List.of(review));

        List<Review> resultado = reviewService.buscarPorAnime(1L);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getAnimeId());

        verify(reviewRepository).findByAnimeId(1L);
    }

    @Test
    @DisplayName("Debe buscar reviews por usuario correctamente")
    void buscarPorUsuario_deberiaRetornarLista() {
        when(reviewRepository.findByUsuarioId(1L)).thenReturn(List.of(review));

        List<Review> resultado = reviewService.buscarPorUsuario(1L);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getUsuarioId());

        verify(reviewRepository).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("Debe retornar reviews top correctamente")
    void topReviews_deberiaRetornarReviewsConPuntuacion10() {
        when(reviewRepository.findByPuntuacion(10)).thenReturn(List.of(review));

        List<Review> resultado = reviewService.topReviews();

        assertNotNull(resultado);
        assertEquals(10, resultado.get(0).getPuntuacion());

        verify(reviewRepository).findByPuntuacion(10);
    }

    @Test
    @DisplayName("Debe calcular promedio por anime correctamente")
    void promedioAnime_deberiaCalcularPromedio() {
        Review review2 = new Review();
        review2.setId(2L);
        review2.setComentario("Bueno");
        review2.setPuntuacion(8);
        review2.setUsuarioId(2L);
        review2.setAnimeId(1L);

        when(reviewRepository.findByAnimeId(1L)).thenReturn(List.of(review, review2));

        Double resultado = reviewService.promedioAnime(1L);

        assertNotNull(resultado);
        assertEquals(9.0, resultado);

        verify(reviewRepository).findByAnimeId(1L);
    }

    @Test
    @DisplayName("Debe actualizar una review correctamente")
    void update_deberiaActualizarReview() {
        Review reviewActualizada = new Review();
        reviewActualizada.setComentario("Actualizada");
        reviewActualizada.setPuntuacion(9);
        reviewActualizada.setUsuarioId(1L);
        reviewActualizada.setAnimeId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review resultado = reviewService.update(1L, reviewActualizada);

        assertNotNull(resultado);
        verify(reviewRepository).findById(1L);
        verify(reviewRepository).save(review);
    }

    @Test
    @DisplayName("Debe eliminar una review por ID correctamente")
    void delete_deberiaEliminarReviewSiExiste() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.delete(1L);

        verify(reviewRepository).findById(1L);
        verify(reviewRepository).delete(review);
    }
}