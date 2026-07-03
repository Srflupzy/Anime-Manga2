package estudio.cl.estudio_service;

import estudio.cl.estudio_service.model.Estudio;
import estudio.cl.estudio_service.repository.EstudioRepository;

import estudio.cl.estudio_service.service.EstudioService;
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
@DisplayName("Pruebas unitarias para EstudioService")
public class EstudioServiceTest {

    @Mock
    private EstudioRepository estudioRepository;

    @InjectMocks
    private EstudioService estudioService;

    private Estudio estudio;

    @BeforeEach
    public void setUp() {
        estudio = new Estudio();
        estudio.setId(1L);
        estudio.setNombre("Ufotable");
        estudio.setPais("Japón");
    }

    @Test
    @DisplayName("Debe listar todos los estudios correctamente")
    public void findAll_deberiaRetornarListaDeEstudios() {
        when(estudioRepository.findAll()).thenReturn(List.of(estudio));

        List<Estudio> resultado = estudioService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ufotable", resultado.get(0).getNombre());

        verify(estudioRepository).findAll();
    }

    @Test
    @DisplayName("Debe buscar un estudio por ID cuando existe")
    public void findById_cuandoExiste_deberiaRetornarEstudio() {
        when(estudioRepository.findById(1L)).thenReturn(Optional.of(estudio));

        Estudio resultado = estudioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ufotable", resultado.getNombre());

        verify(estudioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar NoSuchElementException cuando el estudio por ID no existe")
    public void findById_cuandoNoExiste_deberiaLanzarException() {
        when(estudioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> estudioService.findById(99L)
        );
        verify(estudioRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un estudio correctamente si no está duplicado")
    public void save_deberiaGuardarYRetornarEstudio() {
        when(estudioRepository.existsByNombreIgnoreCase("Ufotable")).thenReturn(false);
        when(estudioRepository.save(any(Estudio.class))).thenReturn(estudio);

        Estudio resultado = estudioService.save(estudio);

        assertNotNull(resultado);
        assertEquals("Ufotable", resultado.getNombre());

        verify(estudioRepository).existsByNombreIgnoreCase("Ufotable");
        verify(estudioRepository).save(estudio);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException al intentar guardar un estudio con nombre ya existente")
    public void save_cuandoYaExiste_deberiaLanzarRuntimeException() {
        when(estudioRepository.existsByNombreIgnoreCase("Ufotable")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> estudioService.save(estudio)
        );

        assertEquals("Ya existe un estudio con ese nombre", exception.getMessage());
        verify(estudioRepository, never()).save(any(Estudio.class));
    }

    @Test
    @DisplayName("Debe actualizar la información de un estudio correctamente")
    public void update_deberiaModificarYGuardarEstudio() {
        Estudio estudioModificado = new Estudio();
        estudioModificado.setNombre("Mappa");
        estudioModificado.setPais("Japón");

        when(estudioRepository.findById(1L)).thenReturn(Optional.of(estudio));
        when(estudioRepository.save(any(Estudio.class))).thenReturn(estudio);

        Estudio resultado = estudioService.update(1L, estudioModificado);

        assertNotNull(resultado);
        verify(estudioRepository).findById(1L);
        verify(estudioRepository).save(estudio);
    }

    @Test
    @DisplayName("Debe eliminar un estudio por ID correctamente")
    public void delete_deberiaEliminarEstudio() {
        doNothing().when(estudioRepository).deleteById(1L);

        estudioService.delete(1L);

        verify(estudioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe buscar un estudio por nombre cuando se encuentra")
    public void buscarPorNombre_cuandoExiste_deberiaRetornarEstudio() {
        when(estudioRepository.findByNombreIgnoreCase("Ufotable")).thenReturn(Optional.of(estudio));

        Estudio resultado = estudioService.buscarPorNombre("Ufotable");

        assertNotNull(resultado);
        assertEquals("Ufotable", resultado.getNombre());

        verify(estudioRepository).findByNombreIgnoreCase("Ufotable");
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el estudio buscado por nombre no se encuentra")
    public void buscarPorNombre_cuandoNoExiste_deberiaLanzarRuntimeException() {
        when(estudioRepository.findByNombreIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> estudioService.buscarPorNombre("Inexistente")
        );

        assertEquals("Estudio no encontrado", exception.getMessage());
        verify(estudioRepository).findByNombreIgnoreCase("Inexistente");
    }
}