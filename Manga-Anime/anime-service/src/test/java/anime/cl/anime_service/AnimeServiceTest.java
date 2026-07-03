package anime.cl.anime_service;

import anime.cl.anime_service.client.EstudioFeing;
import anime.cl.anime_service.client.GeneroFeign;
import anime.cl.anime_service.dto.AnimeDTO;
import anime.cl.anime_service.exception.AnimeNoEncontradoException;
import anime.cl.anime_service.mapper.AnimeMapper;
import anime.cl.anime_service.model.Anime;
import anime.cl.anime_service.repository.AnimeRepository;
import anime.cl.anime_service.service.AnimeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para AnimeService")
public class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private AnimeMapper animeMapper;

    @Mock
    private EstudioFeing estudioClient;

    @Mock
    private GeneroFeign generoFeign;

    @InjectMocks
    private AnimeService animeService;

    private Anime anime;
    private AnimeDTO animeDTO;

    @BeforeEach
    public void setUp() {
        anime = new Anime();
        anime.setId(1L);
        anime.setTitulo("Naruto");
        anime.setDescripcion("Ninja rubio hiperactivo");
        anime.setEstudio("Pierrot");
        anime.setTemporadas(9);
        anime.setGeneroId(3L);

        animeDTO = new AnimeDTO();
        animeDTO.setId(1L);
        animeDTO.setTitulo("Naruto");
        animeDTO.setDescripcion("Ninja rubio hiperactivo");
        animeDTO.setEstudio("Pierrot");
        animeDTO.setTemporadas(9);
        animeDTO.setGenero("Shonen");
    }

    @Test
    @DisplayName("Debe listar todos los animes correctamente")
    public void findAll_deberiaRetornarListaDeAnimesDTO() {
        when(animeRepository.findAll()).thenReturn(List.of(anime));
        when(animeMapper.toDTO(anime)).thenReturn(animeDTO);

        List<AnimeDTO> resultado = animeService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTitulo());

        verify(animeRepository).findAll();
        verify(animeMapper).toDTO(anime);
    }

    @Test
    @DisplayName("Debe buscar un anime por ID cuando existe")
    public void findById_cuandoExiste_deberiaRetornarAnimeDTO() {
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));
        when(animeMapper.toDTO(anime)).thenReturn(animeDTO);

        AnimeDTO resultado = animeService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Naruto", resultado.getTitulo());

        verify(animeRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar AnimeNoEncontradoException cuando el anime no existe")
    public void findById_cuandoNoExiste_deberiaLanzarException() {
        when(animeRepository.findById(99L)).thenReturn(Optional.empty());

        AnimeNoEncontradoException exception = assertThrows(
                AnimeNoEncontradoException.class,
                () -> animeService.findById(99L)
        );

        assertEquals("Anime no encontrado", exception.getMessage());
        verify(animeRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un anime correctamente validando clientes Feign")
    public void save_deberiaGuardarYRetornarAnime() {
        when(animeRepository.existsByTituloIgnoreCase(anyString())).thenReturn(false);
        when(animeRepository.save(any(Anime.class))).thenReturn(anime);

        Anime resultado = animeService.save(anime);

        assertNotNull(resultado);
        assertEquals("Naruto", resultado.getTitulo());

        verify(animeRepository).existsByTituloIgnoreCase("Naruto");
        verify(estudioClient).obtenerEstudio("Pierrot");
        verify(generoFeign).obtenerGenero(3L);
        verify(animeRepository).save(anime);
    }

    @Test
    @DisplayName("Debe buscar animes por título correctamente")
    public void buscarPorTitulo_deberiaRetornarListaFiltrada() {
        when(animeRepository.findByTituloContainingIgnoreCase("Naruto")).thenReturn(List.of(anime));
        when(animeMapper.toDTO(anime)).thenReturn(animeDTO);

        List<AnimeDTO> resultado = animeService.buscarPorTitulo("Naruto");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Naruto", resultado.get(0).getTitulo());

        verify(animeRepository).findByTituloContainingIgnoreCase("Naruto");
    }

    @Test
    @DisplayName("Debe eliminar un anime por ID correctamente si existe")
    public void delete_deberiaEliminarAnimeSiExiste() {
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));

        animeService.delete(1L);

        verify(animeRepository).findById(1L);
        verify(animeRepository).delete(anime);
    }
}
