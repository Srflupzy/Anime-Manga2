package favoritos.cl.favoritos_service;

import favoritos.cl.favoritos_service.dto.FavoritoDTO;
import favoritos.cl.favoritos_service.exception.FavoritosNoEncontradoException;
import favoritos.cl.favoritos_service.mapper.FavoritoMapper;
import favoritos.cl.favoritos_service.model.Favorito;
import favoritos.cl.favoritos_service.repository.FavoritosRepository;
import favoritos.cl.favoritos_service.service.FavoritosService;

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
@DisplayName("Pruebas unitarias para FavoritosService")
public class FavoritosServiceTest {

    @Mock
    private FavoritosRepository favoritosRepository;

    @Mock
    private FavoritoMapper favoritoMapper;

    @InjectMocks
    private FavoritosService favoritosService;

    private Favorito favorito;
    private FavoritoDTO favoritoDTO;

    @BeforeEach
    public void setUp() {
        favorito = new Favorito();
        favorito.setId(1L);
        favorito.setUsuarioId(10L);
        favorito.setAnimeId(100L);

        favoritoDTO = new FavoritoDTO();
        favoritoDTO.setId(1L);
        favoritoDTO.setUsuario("10");
        favoritoDTO.setAnime("100");
    }

    @Test
    @DisplayName("Debe listar todos los favoritos con sus datos detallados correctamente")
    public void findAllConDatos_deberiaRetornarListaDeFavoritosDTO() {
        when(favoritosRepository.findAll()).thenReturn(List.of(favorito));
        when(favoritoMapper.toDTO(favorito)).thenReturn(favoritoDTO);

        List<FavoritoDTO> resultado = favoritosService.findAllConDatos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("10", resultado.get(0).getUsuario());

        verify(favoritosRepository).findAll();
        verify(favoritoMapper).toDTO(favorito);
    }

    @Test
    @DisplayName("Debe buscar un favorito por ID con datos complejos cuando existe")
    public void findByIdConDatos_cuandoExiste_deberiaRetornarFavoritoDTO() {
        when(favoritosRepository.findById(1L)).thenReturn(Optional.of(favorito));
        when(favoritoMapper.toDTO(favorito)).thenReturn(favoritoDTO);

        FavoritoDTO resultado = favoritosService.findByIdConDatos(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("100", resultado.getAnime());

        verify(favoritosRepository).findById(1L);
        verify(favoritoMapper).toDTO(favorito);
    }

    @Test
    @DisplayName("Debe lanzar FavoritosNoEncontradoException si el favorito por ID con datos no existe")
    public void findByIdConDatos_cuandoNoExiste_deberiaLanzarException() {
        when(favoritosRepository.findById(99L)).thenReturn(Optional.empty());

        FavoritosNoEncontradoException exception = assertThrows(
                FavoritosNoEncontradoException.class,
                () -> favoritosService.findByIdConDatos(99L)
        );

        assertEquals("Favorito no encontrado", exception.getMessage());
        verify(favoritosRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un favorito correctamente si no está repetido")
    public void save_deberiaGuardarYRetornarFavorito() {
        when(favoritosRepository.existsByUsuarioIdAndAnimeId(10L, 100L)).thenReturn(false);
        when(favoritosRepository.save(any(Favorito.class))).thenReturn(favorito);

        Favorito resultado = favoritosService.save(favorito);

        assertNotNull(resultado);
        assertNull(resultado.getId()); // Corregido: El servicio real ejecuta favorito.setId(null) al inicio

        verify(favoritosRepository).existsByUsuarioIdAndAnimeId(10L, 100L);
        verify(favoritosRepository).save(favorito);
    }

    @Test
    @DisplayName("Debe actualizar un favorito correctamente si existe")
    public void update_deberiaActualizarYRetornarFavorito() {
        when(favoritosRepository.findById(1L)).thenReturn(Optional.of(favorito));
        when(favoritosRepository.save(any(Favorito.class))).thenReturn(favorito);

        Favorito resultado = favoritosService.update(1L, favorito);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(favoritosRepository).findById(1L);
        verify(favoritosRepository).save(any(Favorito.class));
    }

    @Test
    @DisplayName("Debe eliminar un favorito buscando la entidad completa si existe")
    public void delete_deberiaEliminarFavoritoSiExiste() {
        when(favoritosRepository.findById(1L)).thenReturn(Optional.of(favorito));
        doNothing().when(favoritosRepository).delete(favorito);

        favoritosService.delete(1L);

        verify(favoritosRepository).findById(1L);
        verify(favoritosRepository).delete(favorito);
    }

    @Test
    @DisplayName("Debe buscar una lista de favoritos puros filtrados por anime ID")
    public void buscarPorAnime_deberiaRetornarListaDeFavoritos() {
        when(favoritosRepository.findByAnimeId(100L)).thenReturn(List.of(favorito));

        List<Favorito> resultado = favoritosService.buscarPorAnime(100L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getAnimeId());

        verify(favoritosRepository).findByAnimeId(100L); // Corregido el nombre del repositorio
    }

    @Test
    @DisplayName("Debe retornar el conteo exacto de favoritos para un anime")
    public void conteoFavoritos_deberiaRetornarCantidad() {
        when(favoritosRepository.findByAnimeId(100L)).thenReturn(List.of(favorito, favorito, favorito));

        Long resultado = favoritosService.conteoFavoritos(100L);

        assertNotNull(resultado);
        assertEquals(3L, resultado);

        verify(favoritosRepository).findByAnimeId(100L);
    }
}