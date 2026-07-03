package episodio.cl.episodio_service;

import episodio.cl.episodio_service.controller.EpisodioController;
import episodio.cl.episodio_service.model.Episodio;
import episodio.cl.episodio_service.service.EpisodioService;

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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pruebas en la capa Controller de Episodios (Modo Standalone)")
public class EpisodioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EpisodioService episodioService;

    @InjectMocks
    private EpisodioController episodioController;

    private Episodio episodio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(episodioController).build();

        episodio = new Episodio();
        episodio.setId(1L);
        episodio.setTitulo("Entra Naruto Uzumaki");
        episodio.setDuracion(24);
        episodio.setTemporadaId(2L);
    }

    @Test
    @DisplayName("GET /api/v1/episodios - Debería retornar 200 OK y la lista de episodios")
    void findAll_deberiaRetornarResponseConListaDeEpisodios() throws Exception {
        when(episodioService.findAll()).thenReturn(List.of(episodio));

        mockMvc.perform(get("/api/v1/episodios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Entra Naruto Uzumaki"));
    }

    @Test
    @DisplayName("GET /api/v1/episodios/{id} - Debería retornar 200 OK y el episodio buscado")
    void findById_cuandoExiste_deberiaRetornarResponseConEpisodio() throws Exception {
        when(episodioService.findById(1L)).thenReturn(episodio);

        mockMvc.perform(get("/api/v1/episodios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Entra Naruto Uzumaki"));
    }

    @Test
    @DisplayName("POST /api/v1/episodios - Debería retornar 201 CREATED y el episodio guardado")
    void save_deberiaGuardarYRetornarResponseConEpisodio() throws Exception {
        when(episodioService.save(any(Episodio.class))).thenReturn(episodio);

        String jsonManual = "{\"titulo\":\"Entra Naruto Uzumaki\",\"duracion\":24,\"temporadaId\":2}";

        // SE AGREGA .param("rol", "ADMIN")
        mockMvc.perform(post("/api/v1/episodios")
                        .param("rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Entra Naruto Uzumaki"));
    }

    @Test
    @DisplayName("PUT /api/v1/episodios/{id} - Debería retornar 200 OK y el episodio modificado")
    void update_deberiaActualizarYRetornarResponseConEpisodio() throws Exception {
        when(episodioService.update(eq(1L), any(Episodio.class))).thenReturn(episodio);

        String jsonManual = "{\"titulo\":\"Entra Naruto Uzumaki Modificado\",\"duracion\":25,\"temporadaId\":2}";

        // SE AGREGA .param("rol", "ADMIN")
        mockMvc.perform(put("/api/v1/episodios/1")
                        .param("rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/episodios/buscar - Debería retornar 200 OK y filtrar por título")
    void buscarPorTitulo_deberiaRetornarResponseConListaFiltrada() throws Exception {
        when(episodioService.buscarPorTitulo("Naruto")).thenReturn(List.of(episodio));

        mockMvc.perform(get("/api/v1/episodios/buscar").param("titulo", "Naruto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Entra Naruto Uzumaki"));
    }

    @Test
    @DisplayName("GET /api/v1/episodios/temporada/{temporadaId} - Debería retornar 200 OK y filtrar por temporada")
    void buscarPorTemporada_deberiaRetornarResponsePorTemporadaId() throws Exception {
        when(episodioService.buscarPorTemporada(2L)).thenReturn(List.of(episodio));

        mockMvc.perform(get("/api/v1/episodios/temporada/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temporadaId").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/episodios/mas-largo - Debería retornar 200 OK y el episodio de mayor duración")
    void episodioMasLargo_deberiaRetornarResponseConEpisodioMasLargo() throws Exception {
        when(episodioService.episodioMasLargo()).thenReturn(episodio);

        mockMvc.perform(get("/api/v1/episodios/mas-largo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Entra Naruto Uzumaki"));
    }

    @Test
    @DisplayName("DELETE /api/v1/episodios/{id} - Debería retornar 204 NO CONTENT")
    void delete_deberiaEliminarEpisodioYRetornarNoContent() throws Exception {
        doNothing().when(episodioService).delete(1L);

        // SE AGREGA .param("rol", "ADMIN")
        mockMvc.perform(delete("/api/v1/episodios/1")
                        .param("rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}