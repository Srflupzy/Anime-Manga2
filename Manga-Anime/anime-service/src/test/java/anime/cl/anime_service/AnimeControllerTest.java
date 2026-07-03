package anime.cl.anime_service;

import anime.cl.anime_service.controller.AnimeController;
import anime.cl.anime_service.dto.AnimeDTO;
import anime.cl.anime_service.model.Anime;
import anime.cl.anime_service.service.AnimeService;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pruebas en la capa Controller de Animes (Modo Standalone)")
class AnimeControllerTest {

    // 1. NO SE USA @Autowired. Se define de manera normal
    private MockMvc mockMvc;

    // 2. Usamos @Mock tradicional en lugar de @MockitoBean de Spring
    @Mock
    private AnimeService animeService;

    // 3. Inyectamos los mocks directamente en el controlador
    @InjectMocks
    private AnimeController animeController;

    private Anime anime;
    private Anime animeSinId;
    private AnimeDTO animeDTO;

    @BeforeEach
    void setUp() {
        // Inicializa las anotaciones de Mockito (@Mock y @InjectMocks)
        MockitoAnnotations.openMocks(this);

        // Construcción manual de MockMvc imitando el proyecto de tu compañero
        this.mockMvc = MockMvcBuilders.standaloneSetup(animeController).build();

        anime = new Anime();
        anime.setId(1L);
        anime.setTitulo("Shingeki no Kyojin");
        anime.setDescripcion("Humanidad contra titanes");
        anime.setEstudio("Mappa");
        anime.setTemporadas(4);
        anime.setGeneroId(2L);

        animeSinId = new Anime();
        animeSinId.setId(null);
        animeSinId.setTitulo("Shingeki no Kyojin");
        animeSinId.setDescripcion("Humanidad contra titanes");
        animeSinId.setEstudio("Mappa");
        animeSinId.setTemporadas(4);
        animeSinId.setGeneroId(2L);

        animeDTO = new AnimeDTO();
        animeDTO.setId(1L);
        animeDTO.setTitulo("Shingeki no Kyojin");
        animeDTO.setDescripcion("Humanidad contra titanes");
        animeDTO.setEstudio("Mappa");
        animeDTO.setTemporadas(4);
        animeDTO.setGenero("Acción");
    }

    @Test
    @DisplayName("GET /api/v1/animes - Debería retornar 200 OK y la lista de animes")
    void testEndpointListarTodos() throws Exception {
        when(animeService.findAll()).thenReturn(List.of(animeDTO));

        mockMvc.perform(get("/api/v1/animes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Shingeki no Kyojin"));
    }

    @Test
    @DisplayName("GET /api/v1/animes/{id} - Debería retornar 200 OK y el anime buscado")
    void testEndpointBuscarPorId() throws Exception {
        when(animeService.findById(1L)).thenReturn(animeDTO);

        mockMvc.perform(get("/api/v1/animes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Shingeki no Kyojin"));
    }

    @Test
    @DisplayName("GET /api/v1/animes/buscar - Debería retornar 200 OK y filtrar por título")
    void testEndpointBuscarPorTitulo() throws Exception {
        when(animeService.buscarPorTitulo("Shingeki")).thenReturn(List.of(animeDTO));

        mockMvc.perform(get("/api/v1/animes/buscar").param("titulo", "Shingeki"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Shingeki no Kyojin"));
    }

    @Test
    @DisplayName("GET /api/v1/animes/info - Debería retornar 200 OK e información del servicio")
    void testEndpointInfo() throws Exception {
        mockMvc.perform(get("/api/v1/animes/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("anime-service"));
    }

    @Test
    @DisplayName("POST /api/v1/animes - Debería retornar 201 CREATED si el rol es ADMIN")
    void testEndpointCrearConRolAdmin() throws Exception {
        when(animeService.save(any(Anime.class))).thenReturn(anime);

        String jsonManual = "{\"titulo\":\"Shingeki no Kyojin\",\"descripcion\":\"Humanidad contra titanes\",\"estudio\":\"Mappa\",\"temporadas\":4,\"generoId\":2}";

        mockMvc.perform(post("/api/v1/animes")
                        .param("rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/animes/{id} - Debería retornar 200 OK y el anime actualizado")
    void testEndpointActualizar() throws Exception {
        when(animeService.update(eq(1L), any(Anime.class))).thenReturn(anime);

        String jsonManual = "{\"id\":1,\"titulo\":\"Shingeki no Kyojin\",\"descripcion\":\"Humanidad contra titanes\",\"estudio\":\"Mappa\",\"temporadas\":4,\"generoId\":2}";

        mockMvc.perform(put("/api/v1/animes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/animes/{id} - Debería retornar 204 NO CONTENT si el rol es ADMIN")
    void testEndpointEliminarConRolAdmin() throws Exception {
        mockMvc.perform(delete("/api/v1/animes/1").param("rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}