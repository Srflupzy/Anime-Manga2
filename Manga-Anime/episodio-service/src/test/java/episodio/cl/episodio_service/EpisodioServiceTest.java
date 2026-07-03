package episodio.cl.episodio_service;

import episodio.cl.episodio_service.model.Episodio;
import episodio.cl.episodio_service.repository.EpisodioRepository;

import episodio.cl.episodio_service.service.EpisodioService;
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
@DisplayName("Pruebas unitarias para EpisodioService")
public class EpisodioServiceTest {

    @Mock
    private EpisodioRepository episodioRepository;

    @InjectMocks
    private EpisodioService episodioService;

    private Episodio episodio;

    @BeforeEach
    public void setUp() {
        episodio = new Episodio();
        episodio.setId(1L);
        episodio.setTitulo("Entra Naruto Uzumaki");
        episodio.setDuracion(24);
        episodio.setTemporadaId(2L);
    }

    @Test
    @DisplayName("Debe listar todos los episodios correctamente")
    public void findAll_deberiaRetornarListaDeEpisodios() {
        when(episodioRepository.findAll()).thenReturn(List.of(episodio));

        List<Episodio> resultado = episodioService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Entra Naruto Uzumaki", resultado.get(0).getTitulo());

        verify(episodioRepository).findAll();
    }

    @Test
    @DisplayName("Debe buscar un episodio por ID cuando existe")
    public void findById_cuandoExiste_deberiaRetornarEpisodio() {
        when(episodioRepository.findById(1L)).thenReturn(Optional.of(episodio));

        Episodio resultado = episodioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Entra Naruto Uzumaki", resultado.getTitulo());

        verify(episodioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar NoSuchElementException cuando el episodio no existe")
    public void findById_cuandoNoExiste_deberiaLanzarException() {
        when(episodioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> episodioService.findById(99L)
        );
        verify(episodioRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un episodio correctamente")
    public void save_deberiaGuardarYRetornarEpisodio() {
        when(episodioRepository.existsByTituloIgnoreCaseAndTemporadaId("Entra Naruto Uzumaki", 2L)).thenReturn(false);
        when(episodioRepository.save(any(Episodio.class))).thenReturn(episodio);

        Episodio resultado = episodioService.save(episodio);

        assertNotNull(resultado);
        assertEquals("Entra Naruto Uzumaki", resultado.getTitulo());

        verify(episodioRepository).existsByTituloIgnoreCaseAndTemporadaId("Entra Naruto Uzumaki", 2L);
        verify(episodioRepository).save(episodio);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el episodio ya existe en la temporada")
    public void save_cuandoYaExiste_deberiaLanzarRuntimeException() {
        when(episodioRepository.existsByTituloIgnoreCaseAndTemporadaId("Entra Naruto Uzumaki", 2L)).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> episodioService.save(episodio)
        );

        assertEquals("Ese episodio ya existe en la temporada", exception.getMessage());
        verify(episodioRepository, never()).save(any(Episodio.class));
    }

    @Test
    @DisplayName("Debe actualizar un episodio correctamente")
    public void update_deberiaModificarYGuardarEpisodio() {
        Episodio episodioModificado = new Episodio();
        episodioModificado.setTitulo("Nuevo Titulo");
        episodioModificado.setDuracion(25);
        episodioModificado.setTemporadaId(3L);

        when(episodioRepository.findById(1L)).thenReturn(Optional.of(episodio));
        when(episodioRepository.save(any(Episodio.class))).thenReturn(episodio);

        Episodio resultado = episodioService.update(1L, episodioModificado);

        assertNotNull(resultado);
        verify(episodioRepository).findById(1L);
        verify(episodioRepository).save(episodio);
    }

    @Test
    @DisplayName("Debe eliminar un episodio por ID correctamente")
    public void delete_deberiaEliminarEpisodio() {
        doNothing().when(episodioRepository).deleteById(1L);

        episodioService.delete(1L);

        verify(episodioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe buscar episodios por título correctamente")
    public void buscarPorTitulo_deberiaRetornarListaFiltrada() {
        when(episodioRepository.findByTituloContainingIgnoreCase("Naruto")).thenReturn(List.of(episodio));

        List<Episodio> resultado = episodioService.buscarPorTitulo("Naruto");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Entra Naruto Uzumaki", resultado.get(0).getTitulo());

        verify(episodioRepository).findByTituloContainingIgnoreCase("Naruto");
    }

    @Test
    @DisplayName("Debe buscar episodios por temporada correctamente")
    public void buscarPorTemporada_deberiaRetornarListaPorTemporada() {
        when(episodioRepository.findByTemporadaId(2L)).thenReturn(List.of(episodio));

        List<Episodio> resultado = episodioService.buscarPorTemporada(2L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(episodioRepository).findByTemporadaId(2L);
    }

    @Test
    @DisplayName("Debe obtener el episodio de mayor duración")
    public void episodioMasLargo_deberiaRetornarEpisodioConMasDuracion() {
        when(episodioRepository.findTopByOrderByDuracionDesc()).thenReturn(Optional.of(episodio));

        Episodio resultado = episodioService.episodioMasLargo();

        assertNotNull(resultado);
        assertEquals("Entra Naruto Uzumaki", resultado.getTitulo());

        verify(episodioRepository).findTopByOrderByDuracionDesc();
    }
}