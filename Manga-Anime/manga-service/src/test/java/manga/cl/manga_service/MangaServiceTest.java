package manga.cl.manga_service;

import manga.cl.manga_service.client.GeneroFeign;
import manga.cl.manga_service.dto.GeneroDTO;
import manga.cl.manga_service.dto.MangaDTO;
import manga.cl.manga_service.model.Manga;
import manga.cl.manga_service.repository.MangaRepository;
import manga.cl.manga_service.service.MangaService;

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
@DisplayName("Pruebas unitarias para MangaService")
class MangaServiceTest {

    @Mock
    private MangaRepository mangaRepository;

    @Mock
    private GeneroFeign generoFeign;

    @InjectMocks
    private MangaService mangaService;

    private Manga manga;
    private GeneroDTO generoDTO;

    @BeforeEach
    void setUp() {
        manga = new Manga();
        manga.setId(1L);
        manga.setTitulo("Berserk");
        manga.setAutor("Kentaro Miura");
        manga.setTomos(42);
        manga.setGeneroId(1L);

        generoDTO = new GeneroDTO();
        generoDTO.setId(1L);
        generoDTO.setNombre("Seinen");
    }

    @Test
    @DisplayName("Debe listar todos los mangas con género correctamente")
    void findAllConGenero_deberiaRetornarListaDeMangaDTO() {
        when(mangaRepository.findAll()).thenReturn(List.of(manga));
        when(generoFeign.pedirGenero(1L)).thenReturn(generoDTO);

        List<MangaDTO> resultado = mangaService.findAllConGenero();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Berserk", resultado.get(0).getTitulo());
        assertEquals("Seinen", resultado.get(0).getGenero());

        verify(mangaRepository).findAll();
        verify(generoFeign).pedirGenero(1L);
    }

    @Test
    @DisplayName("Debe buscar manga por ID con género cuando existe")
    void findByIdConGenero_cuandoExiste_deberiaRetornarMangaDTO() {
        when(mangaRepository.findById(1L)).thenReturn(Optional.of(manga));
        when(generoFeign.pedirGenero(1L)).thenReturn(generoDTO);

        MangaDTO resultado = mangaService.findByIdConGenero(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Berserk", resultado.getTitulo());
        assertEquals("Seinen", resultado.getGenero());

        verify(mangaRepository).findById(1L);
        verify(generoFeign).pedirGenero(1L);
    }

    @Test
    @DisplayName("Debe guardar un manga correctamente")
    void save_deberiaGuardarYRetornarManga() {
        when(generoFeign.pedirGenero(1L))
                .thenReturn(generoDTO);
        when(mangaRepository.save(any(Manga.class)))
                .thenReturn(manga);
        Manga resultado = mangaService.save(manga);
        assertNotNull(resultado);
        assertEquals("Berserk", resultado.getTitulo());

        verify(generoFeign).pedirGenero(1L);
        verify(mangaRepository).save(manga);
    }

    @Test
    @DisplayName("Debe buscar mangas por título correctamente")
    void buscarPorTitulo_deberiaRetornarListaFiltrada() {
        when(mangaRepository.findByTituloContainingIgnoreCase("Berserk")).thenReturn(List.of(manga));

        List<Manga> resultado = mangaService.buscarPorTitulo("Berserk");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Berserk", resultado.get(0).getTitulo());

        verify(mangaRepository).findByTituloContainingIgnoreCase("Berserk");
    }

    @Test
    @DisplayName("Debe actualizar un manga correctamente si existe")
    void update_deberiaActualizarMangaSiExiste() {
        Manga mangaActualizado = new Manga();
        mangaActualizado.setTitulo("Berserk Deluxe");
        mangaActualizado.setAutor("Kentaro Miura");
        mangaActualizado.setTomos(14);
        mangaActualizado.setGeneroId(1L);

        when(mangaRepository.findById(1L)).thenReturn(Optional.of(manga));
        when(mangaRepository.save(any(Manga.class))).thenReturn(manga);

        Manga resultado = mangaService.update(1L, mangaActualizado);

        assertNotNull(resultado);
        verify(mangaRepository).findById(1L);
        verify(mangaRepository).save(manga);
    }

    @Test
    @DisplayName("Debe eliminar un manga por ID correctamente si existe")
    void delete_deberiaEliminarMangaSiExiste() {
        mangaService.delete(1L);

        verify(mangaRepository).deleteById(1L);
    }
}