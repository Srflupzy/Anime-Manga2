package genero.cl.genero_service;

import genero.cl.genero_service.controller.GeneroController;
import genero.cl.genero_service.model.Genero;
import genero.cl.genero_service.service.GeneroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para GeneroController")
public class GeneroControllerTest {

    @Mock
    private GeneroService generoService;

    @InjectMocks
    private GeneroController generoController;

    private Genero genero;

    @BeforeEach
    public void setUp() {
        genero = new Genero();
        genero.setId(1L);
        genero.setNombre("Shonen");
    }

    @Test
    @DisplayName("Debe retornar HTTP 200 y el listado de todos los géneros")
    public void listar_deberiaRetornarResponseConListaDeGeneros() {
        when(generoService.findAll()).thenReturn(List.of(genero));

        ResponseEntity<List<Genero>> response = generoController.listar();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Shonen", response.getBody().get(0).getNombre());

        verify(generoService).findAll();
    }

    @Test
    @DisplayName("Debe retornar HTTP 200 y el género buscado por ID")
    public void buscar_deberiaRetornarResponseConGenero() {
        when(generoService.findById(1L)).thenReturn(genero);

        ResponseEntity<Genero> response = generoController.buscar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Shonen", response.getBody().getNombre());

        verify(generoService).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar HTTP 200 y la lista de géneros filtrados por nombre")
    public void buscarPorNombre_deberiaRetornarResponseConListaFiltrada() {
        when(generoService.buscarPorNombre("Shonen")).thenReturn(List.of(genero));

        ResponseEntity<List<Genero>> response = generoController.buscarPorNombre("Shonen");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Shonen", response.getBody().get(0).getNombre());

        verify(generoService).buscarPorNombre("Shonen");
    }

    @Test
    @DisplayName("Debe retornar HTTP 200 con el mapa de información estática del microservicio")
    public void info_deberiaRetornarMapaConInformacion() {
        ResponseEntity<Map<String, String>> response = generoController.info();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("genero-service", response.getBody().get("microservicio"));
        assertEquals("activo", response.getBody().get("estado"));
        assertEquals("1.0", response.getBody().get("version"));
        assertEquals("8094", response.getBody().get("puerto"));

        verifyNoInteractions(generoService);
    }

    @Test
    @DisplayName("Debe retornar HTTP 201 al registrar un nuevo género correctamente")
    public void guardar_deberiaGuardarYRetornarResponseCreated() {
        when(generoService.save(any(Genero.class))).thenReturn(genero);

        ResponseEntity<Genero> response = generoController.guardar(genero);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Shonen", response.getBody().getNombre());

        verify(generoService).save(genero);
    }

    @Test
    @DisplayName("Debe retornar HTTP 200 al actualizar un género existente")
    public void actualizar_deberiaActualizarYRetornarResponseOk() {
        when(generoService.update(eq(1L), any(Genero.class))).thenReturn(genero);

        ResponseEntity<Genero> response = generoController.actualizar(1L, genero);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Shonen", response.getBody().getNombre());

        verify(generoService).update(eq(1L), any(Genero.class));
    }

    @Test
    @DisplayName("Debe retornar HTTP 204 al eliminar un género correctamente")
    public void eliminar_deberiaEliminarGeneroYRetornarNoContent() {
        doNothing().when(generoService).delete(1L);

        ResponseEntity<Void> response = generoController.eliminar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(generoService).delete(1L);
    }
}