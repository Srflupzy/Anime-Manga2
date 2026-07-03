package temporada.cl.temporada_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import temporada.cl.temporada_service.client.AnimeFeign;
import temporada.cl.temporada_service.dto.AnimeDTO;
import temporada.cl.temporada_service.dto.TemporadaDTO;
import temporada.cl.temporada_service.model.Temporada;
import temporada.cl.temporada_service.repository.TemporadaRepository;
import temporada.cl.temporada_service.service.TemporadaService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para TemporadaService")
class TemporadaServiceTest {

    @Mock
    private TemporadaRepository temporadaRepository;

    @Mock
    private AnimeFeign animeFeign;

    @InjectMocks
    private TemporadaService temporadaService;

    private Temporada temporada;
    private AnimeDTO animeDTO;

    @BeforeEach
    void setUp() {
        temporada = new Temporada();
        temporada.setId(1L);
        temporada.setNumero(1);
        temporada.setAnimeId(1L);

        animeDTO = new AnimeDTO();
        animeDTO.setId(1L);
        animeDTO.setTitulo("Demon Slayer");
    }

    @Test
    @DisplayName("Debe listar temporadas con nombre del anime")
    void findAllConNombre_deberiaRetornarListaDTO() {
        when(temporadaRepository.findAll()).thenReturn(List.of(temporada));
        when(animeFeign.buscarAnime(1L)).thenReturn(animeDTO);

        List<TemporadaDTO> resultado = temporadaService.findAllConNombre();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Demon Slayer", resultado.get(0).getAnimeNombre());

        verify(temporadaRepository).findAll();
        verify(animeFeign).buscarAnime(1L);
    }

    @Test
    @DisplayName("Debe buscar temporada por ID con anime")
    void findByIdConAnime_deberiaRetornarDTO() {
        when(temporadaRepository.findById(1L)).thenReturn(Optional.of(temporada));
        when(animeFeign.buscarAnime(1L)).thenReturn(animeDTO);

        TemporadaDTO resultado = temporadaService.findByIdConAnime(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Demon Slayer", resultado.getAnimeNombre());

        verify(temporadaRepository).findById(1L);
        verify(animeFeign).buscarAnime(1L);
    }

    @Test
    @DisplayName("Debe guardar una temporada correctamente")
    void save_deberiaGuardarTemporada() {
        when(temporadaRepository.existsByNumeroAndAnimeId(1, 1L)).thenReturn(false);
        when(animeFeign.buscarAnime(1L)).thenReturn(animeDTO);
        when(temporadaRepository.save(any(Temporada.class))).thenReturn(temporada);

        Temporada resultado = temporadaService.save(temporada);

        assertNotNull(resultado);
        assertEquals(1, resultado.getNumero());

        verify(temporadaRepository).existsByNumeroAndAnimeId(1, 1L);
        verify(animeFeign).buscarAnime(1L);
        verify(temporadaRepository).save(temporada);
    }

    @Test
    @DisplayName("Debe buscar temporadas por número")
    void buscarPorNumero_deberiaRetornarLista() {
        when(temporadaRepository.findByNumero(1)).thenReturn(List.of(temporada));

        List<Temporada> resultado = temporadaService.buscarPorNumero(1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        verify(temporadaRepository).findByNumero(1);
    }

    @Test
    @DisplayName("Debe buscar temporadas por anime con nombre")
    void buscarPorAnimeConNombre_deberiaRetornarListaDTO() {
        when(temporadaRepository.findByAnimeId(1L)).thenReturn(List.of(temporada));
        when(animeFeign.buscarAnime(1L)).thenReturn(animeDTO);

        List<TemporadaDTO> resultado = temporadaService.buscarPorAnimeConNombre(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Demon Slayer", resultado.get(0).getAnimeNombre());

        verify(temporadaRepository).findByAnimeId(1L);
        verify(animeFeign).buscarAnime(1L);
    }

    @Test
    @DisplayName("Debe buscar temporada específica")
    void buscarTemporadaEspecifica_deberiaRetornarTemporada() {
        when(temporadaRepository.findByAnimeIdAndNumero(1L, 1)).thenReturn(Optional.of(temporada));

        Temporada resultado = temporadaService.buscarTemporadaEspecifica(1L, 1);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getAnimeId());

        verify(temporadaRepository).findByAnimeIdAndNumero(1L, 1);
    }

    @Test
    @DisplayName("Debe actualizar una temporada correctamente")
    void update_deberiaActualizarTemporada() {
        Temporada temporadaActualizada = new Temporada();
        temporadaActualizada.setNumero(2);
        temporadaActualizada.setAnimeId(1L);

        when(temporadaRepository.findById(1L)).thenReturn(Optional.of(temporada));
        when(temporadaRepository.save(any(Temporada.class))).thenReturn(temporada);

        Temporada resultado = temporadaService.update(1L, temporadaActualizada);

        assertNotNull(resultado);

        verify(temporadaRepository).findById(1L);
        verify(temporadaRepository).save(temporada);
    }
    @Test
    @DisplayName("Debe eliminar una temporada por ID")
    void delete_deberiaEliminarTemporada() {
        when(temporadaRepository.existsById(1L)).thenReturn(true);

        temporadaService.delete(1L);

        verify(temporadaRepository).existsById(1L);
        verify(temporadaRepository).deleteById(1L);
    }
}