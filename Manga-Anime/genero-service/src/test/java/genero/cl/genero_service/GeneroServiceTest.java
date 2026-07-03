package genero.cl.genero_service;

import genero.cl.genero_service.model.Genero;
import genero.cl.genero_service.repository.GeneroRepository;

import genero.cl.genero_service.service.GeneroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para GeneroService")
public class GeneroServiceTest {

    @Mock
    private GeneroRepository generoRepository;

    @InjectMocks
    private GeneroService generoService;

    private Genero genero;

    @BeforeEach
    public void setUp() {
        genero = new Genero();
        genero.setId(1L);
        genero.setNombre("Shonen");
        genero.setDescripcion("Anime dirigido a un público joven masculino");
    }

    @Test
    @DisplayName("Debe listar todos los géneros correctamente")
    public void findAll_deberiaRetornarListaDeGeneros() {
        when(generoRepository.findAll()).thenReturn(List.of(genero));

        List<Genero> resultado = generoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Shonen", resultado.get(0).getNombre());

        verify(generoRepository).findAll();
    }

    @Test
    @DisplayName("Debe buscar un género por ID cuando existe")
    public void findById_cuandoExiste_deberiaRetornarGenero() {
        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));

        Genero resultado = generoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Shonen", resultado.getNombre());

        verify(generoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar NoSuchElementException cuando el género por ID no existe")
    public void findById_cuandoNoExiste_deberiaLanzarException() {
        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> generoService.findById(99L)
        );
        verify(generoRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un género correctamente si no está duplicado")
    public void save_deberiaGuardarYRetornarGenero() {
        when(generoRepository.existsByNombreIgnoreCase("Shonen")).thenReturn(false);
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);

        Genero resultado = generoService.save(genero);

        assertNotNull(resultado);
        assertEquals("Shonen", resultado.getNombre());

        verify(generoRepository).existsByNombreIgnoreCase("Shonen");
        verify(generoRepository).save(genero);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException al intentar guardar un género con nombre ya existente")
    public void save_cuandoYaExiste_deberiaLanzarRuntimeException() {
        when(generoRepository.existsByNombreIgnoreCase("Shonen")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> generoService.save(genero)
        );

        assertEquals("El genero ya existe", exception.getMessage());
        verify(generoRepository, never()).save(any(Genero.class));
    }

    @Test
    @DisplayName("Debe actualizar la información de un género correctamente")
    public void update_deberiaModificarYGuardarGenero() {
        Genero generoModificado = new Genero();
        generoModificado.setNombre("Seinen");
        generoModificado.setDescripcion("Anime dirigido a adultos");

        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);

        Genero resultado = generoService.update(1L, generoModificado);

        assertNotNull(resultado);
        verify(generoRepository).findById(1L);
        verify(generoRepository).save(genero);
    }

    @Test
    @DisplayName("Debe eliminar un género por ID correctamente")
    public void delete_deberiaEliminarGenero() {
        doNothing().when(generoRepository).deleteById(1L);

        generoService.delete(1L);

        verify(generoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe buscar géneros por coincidencia de nombre correctamente")
    public void buscarPorNombre_deberiaRetornarListaFiltrada() {
        when(generoRepository.findByNombreContainingIgnoreCase("Sho")).thenReturn(List.of(genero));

        List<Genero> resultado = generoService.buscarPorNombre("Sho");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Shonen", resultado.get(0).getNombre());

        verify(generoRepository).findByNombreContainingIgnoreCase("Sho");
    }
}