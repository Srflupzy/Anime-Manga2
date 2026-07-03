package review.cl.review_service;

import review.cl.review_service.controller.ReviewController;
import review.cl.review_service.dto.ReviewDTO;
import review.cl.review_service.model.Review;
import review.cl.review_service.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pruebas en la capa Controller de Reviews")
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        review = new Review();
        review.setId(1L);
        review.setComentario("Muy buen anime");
        review.setPuntuacion(10);
        review.setUsuarioId(1L);
        review.setAnimeId(1L);

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setComentario("Muy buen anime");
        reviewDTO.setPuntuacion(10);
        reviewDTO.setUsuario("Fernando");
        reviewDTO.setAnime("Demon Slayer");
    }

    @Test
    @DisplayName("GET /api/v1/reviews - Debería retornar 200 OK y la lista de reviews")
    void testListar() throws Exception {
        when(reviewService.findAll()).thenReturn(List.of(reviewDTO));

        mockMvc.perform(get("/api/v1/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Muy buen anime"))
                .andExpect(jsonPath("$[0].puntuacion").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/reviews/{id} - Debería retornar 200 OK y la review buscada")
    void testBuscarPorId() throws Exception {
        when(reviewService.findById(1L)).thenReturn(reviewDTO);

        mockMvc.perform(get("/api/v1/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Muy buen anime"))
                .andExpect(jsonPath("$.puntuacion").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/reviews/anime/{animeId} - Debería retornar 200 OK")
    void testBuscarPorAnime() throws Exception {
        when(reviewService.buscarPorAnime(1L)).thenReturn(List.of(review));

        mockMvc.perform(get("/api/v1/reviews/anime/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Muy buen anime"));
    }

    @Test
    @DisplayName("GET /api/v1/reviews/usuario/{usuarioId} - Debería retornar 200 OK")
    void testBuscarPorUsuario() throws Exception {
        when(reviewService.buscarPorUsuario(1L)).thenReturn(List.of(review));

        mockMvc.perform(get("/api/v1/reviews/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Muy buen anime"));
    }

    @Test
    @DisplayName("GET /api/v1/reviews/top - Debería retornar 200 OK")
    void testTopReviews() throws Exception {
        when(reviewService.topReviews()).thenReturn(List.of(review));

        mockMvc.perform(get("/api/v1/reviews/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].puntuacion").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/reviews/promedio/{animeId} - Debería retornar 200 OK")
    void testPromedioAnime() throws Exception {
        when(reviewService.promedioAnime(1L)).thenReturn(9.5);

        mockMvc.perform(get("/api/v1/reviews/promedio/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("9.5"));
    }

    @Test
    @DisplayName("GET /api/v1/reviews/info - Debería retornar 200 OK")
    void testInfo() throws Exception {
        mockMvc.perform(get("/api/v1/reviews/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("review-service"));
    }

    @Test
    @DisplayName("POST /api/v1/reviews - Debería retornar 201 CREATED")
    void testGuardar() throws Exception {
        when(reviewService.save(any(Review.class))).thenReturn(review);

        String json = "{\"comentario\":\"Muy buen anime\",\"puntuacion\":10,\"usuarioId\":1,\"animeId\":1}";

        mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{id} - Debería retornar 200 OK")
    void testActualizar() throws Exception {
        when(reviewService.update(eq(1L), any(Review.class))).thenReturn(review);

        String json = "{\"comentario\":\"Muy buen anime\",\"puntuacion\":10,\"usuarioId\":1,\"animeId\":1}";

        mockMvc.perform(put("/api/v1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{id} - Debería retornar 204 NO CONTENT")
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/v1/reviews/1"))
                .andExpect(status().isNoContent());
    }
}