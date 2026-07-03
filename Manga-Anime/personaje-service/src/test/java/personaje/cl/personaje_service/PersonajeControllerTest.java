package personaje.cl.personaje_service;

import personaje.cl.personaje_service.controller.PersonajeController;
import personaje.cl.personaje_service.dto.PersonajeDTO;
import personaje.cl.personaje_service.model.Personaje;
import personaje.cl.personaje_service.service.PersonajeService;

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

@DisplayName("Pruebas en la capa Controller de Personajes")
class PersonajeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonajeService personajeService;

    @InjectMocks
    private PersonajeController personajeController;

    private Personaje personaje;
    private PersonajeDTO personajeDTO;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(personajeController)
                .build();

        personaje = new Personaje();
        personaje.setId(1L);
        personaje.setNombre("Guts");
        personaje.setRol("Protagonista");
        personaje.setAnimeId(1L);

        personajeDTO = new PersonajeDTO();
        personajeDTO.setId(1L);
        personajeDTO.setNombre("Guts");
        personajeDTO.setRol("Protagonista");
        personajeDTO.setAnimeNombre("Berserk");
    }

    @Test
    @DisplayName("GET /personajes")
    void testListar() throws Exception {

        when(personajeService.findAllConAnime())
                .thenReturn(List.of(personajeDTO));

        mockMvc.perform(get("/api/v1/personajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre")
                        .value("Guts"));
    }

    @Test
    @DisplayName("GET /personajes/{id}")
    void testBuscarPorId() throws Exception {

        when(personajeService.findByIdConAnime(1L))
                .thenReturn(personajeDTO);

        mockMvc.perform(get("/api/v1/personajes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Guts"));
    }

    @Test
    @DisplayName("GET /personajes/buscar")
    void testBuscarPorNombre() throws Exception {

        when(personajeService.buscarPorNombre("Guts"))
                .thenReturn(List.of(personaje));

        mockMvc.perform(
                        get("/api/v1/personajes/buscar")
                                .param("nombre", "Guts")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre")
                        .value("Guts"));
    }

    @Test
    @DisplayName("GET /personajes/info")
    void testInfo() throws Exception {

        mockMvc.perform(get("/api/v1/personajes/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio")
                        .value("personaje-service"));
    }

    @Test
    @DisplayName("POST /personajes")
    void testGuardar() throws Exception {

        when(personajeService.save(any(Personaje.class)))
                .thenReturn(personaje);

        String json = """
                {
                  "nombre":"Guts",
                  "rol":"Protagonista",
                  "animeId":1
                }
                """;

        mockMvc.perform(
                        post("/api/v1/personajes")
                                .param("rol","ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT /personajes/{id}")
    void testActualizar() throws Exception {

        when(personajeService.update(
                eq(1L),
                any(Personaje.class)))
                .thenReturn(personaje);

        String json = """
                {
                  "nombre":"Guts",
                  "rol":"Protagonista",
                  "animeId":1
                }
                """;

        mockMvc.perform(
                        put("/api/v1/personajes/1")
                                .param("rol","ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /personajes/{id}")
    void testEliminar() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/personajes/1")
                                .param("rol","ADMIN")
                )
                .andExpect(status().isNoContent());
    }
}