package personaje.cl.personaje_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import personaje.cl.personaje_service.client.AnimeFeign;
import personaje.cl.personaje_service.dto.AnimeDTO;
import personaje.cl.personaje_service.dto.PersonajeDTO;
import personaje.cl.personaje_service.model.Personaje;
import personaje.cl.personaje_service.repository.PersonajeRepository;
import personaje.cl.personaje_service.service.PersonajeService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PersonajeService")
class PersonajeServiceTest {

    @Mock
    private PersonajeRepository personajeRepository;

    @Mock
    private AnimeFeign animeFeign;

    @InjectMocks
    private PersonajeService personajeService;

    private Personaje personaje;
    private AnimeDTO animeDTO;

    @BeforeEach
    void setUp() {
        personaje = new Personaje();
        personaje.setId(1L);
        personaje.setNombre("Guts");
        personaje.setRol("Protagonista");
        personaje.setAnimeId(1L);

        animeDTO = new AnimeDTO();
        animeDTO.setId(1L);
        animeDTO.setTitulo("Berserk");
    }

    @Test
    @DisplayName("Debe listar todos los personajes con anime correctamente")
    void findAllConAnime_deberiaRetornarListaDePersonajeDTO() {
        when(personajeRepository.findAll()).thenReturn(List.of(personaje));
        when(animeFeign.pedirAnime(1L)).thenReturn(animeDTO);

        List<PersonajeDTO> resultado = personajeService.findAllConAnime();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Guts", resultado.get(0).getNombre());
        assertEquals("Berserk", resultado.get(0).getAnimeNombre());

        verify(personajeRepository).findAll();
        verify(animeFeign).pedirAnime(1L);
    }

    @Test
    @DisplayName("Debe buscar personaje por ID con anime cuando existe")
    void findByIdConAnime_cuandoExiste_deberiaRetornarPersonajeDTO() {
        when(personajeRepository.findById(1L)).thenReturn(Optional.of(personaje));
        when(animeFeign.pedirAnime(1L)).thenReturn(animeDTO);

        PersonajeDTO resultado = personajeService.findByIdConAnime(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Guts", resultado.getNombre());
        assertEquals("Berserk", resultado.getAnimeNombre());

        verify(personajeRepository).findById(1L);
        verify(animeFeign).pedirAnime(1L);
    }

    @Test
    @DisplayName("Debe guardar un personaje correctamente")
    void save_deberiaGuardarYRetornarPersonaje() {
        when(animeFeign.pedirAnime(1L)).thenReturn(animeDTO);
        when(personajeRepository.save(any(Personaje.class))).thenReturn(personaje);

        Personaje resultado = personajeService.save(personaje);

        assertNotNull(resultado);
        assertEquals("Guts", resultado.getNombre());

        verify(animeFeign).pedirAnime(1L);
        verify(personajeRepository).save(personaje);
    }

    @Test
    @DisplayName("Debe buscar personajes por nombre correctamente")
    void buscarPorNombre_deberiaRetornarListaFiltrada() {
        when(personajeRepository.findByNombreContainingIgnoreCase("Guts"))
                .thenReturn(List.of(personaje));

        List<Personaje> resultado = personajeService.buscarPorNombre("Guts");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Guts", resultado.get(0).getNombre());

        verify(personajeRepository).findByNombreContainingIgnoreCase("Guts");
    }

    @Test
    @DisplayName("Debe actualizar un personaje correctamente si existe")
    void update_deberiaActualizarPersonajeSiExiste() {
        Personaje personajeActualizado = new Personaje();
        personajeActualizado.setNombre("Guts Actualizado");
        personajeActualizado.setRol("Espadachín");
        personajeActualizado.setAnimeId(1L);

        when(personajeRepository.findById(1L)).thenReturn(Optional.of(personaje));
        when(personajeRepository.save(any(Personaje.class))).thenReturn(personaje);

        Personaje resultado = personajeService.update(1L, personajeActualizado);

        assertNotNull(resultado);
        verify(personajeRepository).findById(1L);
        verify(personajeRepository).save(personaje);
    }

    @Test
    @DisplayName("Debe eliminar un personaje por ID correctamente")
    void delete_deberiaEliminarPersonaje() {
        personajeService.delete(1L);

        verify(personajeRepository).deleteById(1L);
    }
}