package temporada.cl.temporada_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import temporada.cl.temporada_service.controller.TemporadaController;
import temporada.cl.temporada_service.dto.TemporadaDTO;
import temporada.cl.temporada_service.model.Temporada;
import temporada.cl.temporada_service.service.TemporadaService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pruebas en la capa Controller de Temporadas")
class TemporadaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TemporadaService temporadaService;

    @InjectMocks
    private TemporadaController temporadaController;

    private Temporada temporada;
    private TemporadaDTO temporadaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(temporadaController).build();

        temporada = new Temporada();
        temporada.setId(1L);
        temporada.setNumero(1);
        temporada.setAnimeId(1L);

        temporadaDTO = new TemporadaDTO();
        temporadaDTO.setId(1L);
        temporadaDTO.setNumeroTemporada(1);
        temporadaDTO.setAnimeNombre("Demon Slayer");
    }

    @Test
    @DisplayName("GET /api/v1/temporadas - Debería retornar 200 OK")
    void testListar() throws Exception {
        when(temporadaService.findAllConNombre()).thenReturn(List.of(temporadaDTO));

        mockMvc.perform(get("/api/v1/temporadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].animeNombre").value("Demon Slayer"));
    }

    @Test
    @DisplayName("GET /api/v1/temporadas/{id} - Debería retornar 200 OK")
    void testBuscarPorId() throws Exception {
        when(temporadaService.findByIdConAnime(1L)).thenReturn(temporadaDTO);

        mockMvc.perform(get("/api/v1/temporadas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.animeNombre").value("Demon Slayer"));
    }

    @Test
    @DisplayName("GET /api/v1/temporadas/buscar - Debería retornar 200 OK")
    void testBuscarPorNumero() throws Exception {
        when(temporadaService.buscarPorNumero(1)).thenReturn(List.of(temporada));

        mockMvc.perform(get("/api/v1/temporadas/buscar").param("numero", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/temporadas/anime/{animeId} - Debería retornar 200 OK")
    void testBuscarPorAnime() throws Exception {
        when(temporadaService.buscarPorAnimeConNombre(1L)).thenReturn(List.of(temporadaDTO));

        mockMvc.perform(get("/api/v1/temporadas/anime/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animeNombre").value("Demon Slayer"));
    }

    @Test
    @DisplayName("GET /api/v1/temporadas/anime/{animeId}/numero/{numero} - Debería retornar 200 OK")
    void testBuscarTemporadaEspecifica() throws Exception {
        when(temporadaService.buscarTemporadaEspecifica(1L, 1)).thenReturn(temporada);

        mockMvc.perform(get("/api/v1/temporadas/anime/1/numero/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/temporadas/info - Debería retornar 200 OK")
    void testInfo() throws Exception {
        mockMvc.perform(get("/api/v1/temporadas/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("temporada-service"));
    }

    @Test
    @DisplayName("POST /api/v1/temporadas - Debería retornar 201 CREATED")
    void testGuardar() throws Exception {
        when(temporadaService.save(any(Temporada.class))).thenReturn(temporada);

        String json = "{\"numero\":1,\"animeId\":1}";

        mockMvc.perform(post("/api/v1/temporadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/temporadas/{id} - Debería retornar 200 OK")
    void testActualizar() throws Exception {
        when(temporadaService.update(eq(1L), any(Temporada.class))).thenReturn(temporada);

        String json = "{\"numero\":1,\"animeId\":1}";

        mockMvc.perform(put("/api/v1/temporadas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/temporadas/{id} - Debería retornar 204 NO CONTENT")
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/v1/temporadas/1"))
                .andExpect(status().isNoContent());
    }
}