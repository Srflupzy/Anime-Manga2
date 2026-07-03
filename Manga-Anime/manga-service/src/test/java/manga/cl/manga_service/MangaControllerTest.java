package manga.cl.manga_service;

import manga.cl.manga_service.controller.MangaController;
import manga.cl.manga_service.dto.MangaDTO;
import manga.cl.manga_service.model.Manga;
import manga.cl.manga_service.service.MangaService;

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

@DisplayName("Pruebas en la capa Controller de Mangas (Modo Standalone)")
class MangaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MangaService mangaService;

    @InjectMocks
    private MangaController mangaController;

    private Manga manga;
    private MangaDTO mangaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(mangaController).build();

        manga = new Manga();
        manga.setId(1L);
        manga.setTitulo("Berserk");
        manga.setAutor("Kentaro Miura");
        manga.setTomos(42);
        manga.setGeneroId(1L);

        mangaDTO = new MangaDTO();
        mangaDTO.setId(1L);
        mangaDTO.setTitulo("Berserk");
        mangaDTO.setAutor("Kentaro Miura");
        mangaDTO.setTomos(42);
        mangaDTO.setGenero("Seinen");
    }

    @Test
    @DisplayName("GET /api/v1/mangas - Debería retornar 200 OK y la lista de mangas")
    void testEndpointListarTodos() throws Exception {
        when(mangaService.findAllConGenero()).thenReturn(List.of(mangaDTO));

        mockMvc.perform(get("/api/v1/mangas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Berserk"));
    }

    @Test
    @DisplayName("GET /api/v1/mangas/{id} - Debería retornar 200 OK y el manga buscado")
    void testEndpointBuscarPorId() throws Exception {
        when(mangaService.findByIdConGenero(1L)).thenReturn(mangaDTO);

        mockMvc.perform(get("/api/v1/mangas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Berserk"));
    }

    @Test
    @DisplayName("GET /api/v1/mangas/buscar - Debería retornar 200 OK y filtrar por título")
    void testEndpointBuscarPorTitulo() throws Exception {
        when(mangaService.buscarPorTitulo("Berserk")).thenReturn(List.of(manga));

        mockMvc.perform(get("/api/v1/mangas/buscar").param("titulo", "Berserk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Berserk"));
    }

    @Test
    @DisplayName("GET /api/v1/mangas/info - Debería retornar 200 OK")
    void testEndpointInfo() throws Exception {
        mockMvc.perform(get("/api/v1/mangas/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("manga-service"));
    }

    @Test
    @DisplayName("POST /api/v1/mangas - Debería retornar 201 CREATED")
    void testEndpointCrear() throws Exception {
        when(mangaService.save(any(Manga.class))).thenReturn(manga);

        String jsonManual = "{\"titulo\":\"Berserk\",\"autor\":\"Kentaro Miura\",\"tomos\":42,\"generoId\":1}";

        mockMvc.perform(post("/api/v1/mangas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/mangas/{id} - Debería retornar 200 OK")
    void testEndpointActualizar() throws Exception {
        when(mangaService.update(eq(1L), any(Manga.class))).thenReturn(manga);

        String jsonManual = "{\"id\":1,\"titulo\":\"Berserk\",\"autor\":\"Kentaro Miura\",\"tomos\":42,\"generoId\":1}";

        mockMvc.perform(put("/api/v1/mangas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/mangas/{id} - Debería retornar 204 NO CONTENT")
    void testEndpointEliminar() throws Exception {
        mockMvc.perform(delete("/api/v1/mangas/1"))
                .andExpect(status().isNoContent());
    }
}