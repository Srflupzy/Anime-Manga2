package estudio.cl.estudio_service;

import estudio.cl.estudio_service.controller.EstudioController;
import estudio.cl.estudio_service.model.Estudio;
import estudio.cl.estudio_service.service.EstudioService;

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

@DisplayName("Pruebas en la capa Controller de Estudios (Modo Standalone)")
public class EstudioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EstudioService estudioService;

    @InjectMocks
    private EstudioController estudioController;

    private Estudio estudio;

    @BeforeEach
    void setUp() {
        // Inicializa las anotaciones de Mockito
        MockitoAnnotations.openMocks(this);

        // Construcción de MockMvc en modo Standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(estudioController).build();

        estudio = new Estudio();
        estudio.setId(1L);
        estudio.setNombre("Ufotable");
        estudio.setPais("Japón"); // Adaptado a los campos de tu EstudioService real
    }

    @Test
    @DisplayName("GET /api/v1/estudios - Debería retornar 200 OK y la lista de estudios")
    void listar_deberiaRetornarResponseConListaDeEstudios() throws Exception {
        when(estudioService.findAll()).thenReturn(List.of(estudio));

        mockMvc.perform(get("/api/v1/estudios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ufotable"));
    }

    @Test
    @DisplayName("GET /api/v1/estudios/{id} - Debería retornar 200 OK y el estudio buscado por ID")
    void buscar_deberiaRetornarResponseConEstudio() throws Exception {
        when(estudioService.findById(1L)).thenReturn(estudio);

        mockMvc.perform(get("/api/v1/estudios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ufotable"));
    }

    @Test
    @DisplayName("GET /api/v1/estudios/buscar - Debería retornar 200 OK y el estudio buscado por nombre")
    void buscarPorNombre_deberiaRetornarResponseConEstudio() throws Exception {
        when(estudioService.buscarPorNombre("Ufotable")).thenReturn(estudio);

        mockMvc.perform(get("/api/v1/estudios/buscar").param("nombre", "Ufotable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ufotable"));
    }

    @Test
    @DisplayName("GET /api/v1/estudios/info - Debería retornar 200 OK e información del servicio")
    void info_deberiaRetornarMapaConInformacion() throws Exception {
        mockMvc.perform(get("/api/v1/estudios/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("estudio-service"));
    }

    @Test
    @DisplayName("POST /api/v1/estudios - Debería retornar 201 CREATED si el rol es ADMIN")
    void guardar_deberiaGuardarYRetornarResponseCreated() throws Exception {
        when(estudioService.save(any(Estudio.class))).thenReturn(estudio);

        String jsonManual = "{\"nombre\":\"Ufotable\",\"pais\":\"Japón\"}";

        mockMvc.perform(post("/api/v1/estudios")
                        .param("rol", "ADMIN") // Se agrega el parámetro obligatorio
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ufotable"));
    }

    @Test
    @DisplayName("PUT /api/v1/estudios/{id} - Debería retornar 200 OK y el estudio actualizado")
    void actualizar_deberiaActualizarYRetornarResponseOk() throws Exception {
        when(estudioService.update(eq(1L), any(Estudio.class))).thenReturn(estudio);

        String jsonManual = "{\"nombre\":\"Ufotable\",\"pais\":\"Japón\"}";

        mockMvc.perform(put("/api/v1/estudios/1")
                        .param("rol", "ADMIN") // Se agrega el parámetro obligatorio
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/estudios/{id} - Debería retornar 204 NO CONTENT si el rol es ADMIN")
    void eliminar_deberiaEliminarEstudioYRetornarNoContent() throws Exception {
        doNothing().when(estudioService).delete(1L);

        mockMvc.perform(delete("/api/v1/estudios/1")
                        .param("rol", "ADMIN")) // Se agrega el parámetro obligatorio
                .andExpect(status().isNoContent());
    }
}