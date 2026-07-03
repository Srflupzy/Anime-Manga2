package favoritos.cl.favoritos_service;

import favoritos.cl.favoritos_service.controller.FavoritosController;
import favoritos.cl.favoritos_service.dto.FavoritoDTO;
import favoritos.cl.favoritos_service.model.Favorito;
import favoritos.cl.favoritos_service.service.FavoritosService;

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

@DisplayName("Pruebas en la capa Controller de Favoritos (Modo Standalone)")
public class FavoritosControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoritosService favoritoService;

    @InjectMocks
    private FavoritosController favoritosController;

    private Favorito favorito;
    private FavoritoDTO favoritoDTO;

    @BeforeEach
    void setUp() {
        // Inicializa las anotaciones de Mockito
        MockitoAnnotations.openMocks(this);

        // Construcción de MockMvc en modo Standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(favoritosController).build();

        // Para Favorito usamos el constructor numérico tradicional
        favorito = new Favorito(1L, 10L, 100L);

        // CORRECCIÓN: Para FavoritoDTO pasamos los textos ("10", "100") según su firma (Long, String, String)
        favoritoDTO = new FavoritoDTO(1L, "10", "100");
    }

    @Test
    @DisplayName("GET /api/v1/favoritos - Debería retornar 200 OK y la lista de favoritos")
    void listar_deberiaRetornarResponseConListaDeFavoritosDTO() throws Exception {
        when(favoritoService.findAllConDatos()).thenReturn(List.of(favoritoDTO));

        mockMvc.perform(get("/api/v1/favoritos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value("10"));
    }

    @Test
    @DisplayName("GET /api/v1/favoritos/{id} - Debería retornar 200 OK y el favorito por ID")
    void buscar_deberiaRetornarResponseConFavoritoDTO() throws Exception {
        when(favoritoService.findByIdConDatos(1L)).thenReturn(favoritoDTO);

        mockMvc.perform(get("/api/v1/favoritos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value("10"));
    }

    @Test
    @DisplayName("GET /api/v1/favoritos/usuario/{usuarioId} - Debería retornar 200 OK filtrado por usuario")
    void buscarPorUsuario_deberiaRetornarResponseConListaDeFavoritosDTO() throws Exception {
        when(favoritoService.findAllConDatos()).thenReturn(List.of(favoritoDTO));

        mockMvc.perform(get("/api/v1/favoritos/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/favoritos/anime/{animeId} - Debería retornar 200 OK filtrado por anime")
    void buscarPorAnime_deberiaRetornarResponseConListaDeFavoritos() throws Exception { // CORRECCIÓN: Espacio eliminado en el nombre del método
        when(favoritoService.buscarPorAnime(100L)).thenReturn(List.of(favorito));

        mockMvc.perform(get("/api/v1/favoritos/anime/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animeId").value(100));
    }

    @Test
    @DisplayName("GET /api/v1/favoritos/conteo/{animeId} - Debería retornar 200 OK con la cantidad")
    void conteoFavoritos_deberiaRetornarCantidadDeFavoritos() throws Exception {
        when(favoritoService.conteoFavoritos(100L)).thenReturn(5L);

        mockMvc.perform(get("/api/v1/favoritos/conteo/100"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @DisplayName("GET /api/v1/favoritos/info - Debería retornar 200 OK e información del servicio")
    void info_deberiaRetornarMapaConInformacion() throws Exception {
        mockMvc.perform(get("/api/v1/favoritos/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("favoritos-service"));
    }

    @Test
    @DisplayName("POST /api/v1/favoritos - Debería retornar 201 CREATED si el rol es ADMIN")
    void guardar_deberiaGuardarYRetornarResponseCreated() throws Exception {
        when(favoritoService.save(any(Favorito.class))).thenReturn(favorito);

        String jsonManual = "{\"usuarioId\":10,\"animeId\":100}";

        mockMvc.perform(post("/api/v1/favoritos")
                        .param("rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/favoritos/{id} - Debería retornar 200 OK al actualizar")
    void actualizar_deberiaActualizarYRetornarResponseOk() throws Exception {
        when(favoritoService.update(eq(1L), any(Favorito.class))).thenReturn(favorito);

        String jsonManual = "{\"usuarioId\":10,\"animeId\":100}";

        mockMvc.perform(put("/api/v1/favoritos/1")
                        .param("rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonManual))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/favoritos/{id} - Debería retornar 204 NO CONTENT")
    void eliminar_deberiaEliminarFavoritoYRetornarNoContent() throws Exception {
        doNothing().when(favoritoService).delete(1L);

        mockMvc.perform(delete("/api/v1/favoritos/1")
                        .param("rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}