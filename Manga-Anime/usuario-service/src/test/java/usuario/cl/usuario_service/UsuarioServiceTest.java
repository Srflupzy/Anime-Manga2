package usuario.cl.usuario_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usuario.cl.usuario_service.exception.EmailDuplicadoException;
import usuario.cl.usuario_service.model.Usuario;
import usuario.cl.usuario_service.repository.UsuarioRepository;
import usuario.cl.usuario_service.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Fernando");
        usuario.setEmail("fernando@gmail.com");
        usuario.setClave("1234");
        usuario.setRol(Rol.ADMIN);
    }

    @Test
    @DisplayName("Debe listar todos los usuarios correctamente")
    void findAll_deberiaRetornarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Fernando", resultado.get(0).getNombre());

        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Debe buscar usuario por ID cuando existe")
    void findById_cuandoExiste_deberiaRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Fernando", resultado.getNombre());

        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe guardar usuario correctamente si el email no existe")
    void save_deberiaGuardarUsuario() {
        when(usuarioRepository.existsByEmailIgnoreCase("fernando@gmail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.save(usuario);

        assertNotNull(resultado);
        assertEquals("fernando@gmail.com", resultado.getEmail());

        verify(usuarioRepository).existsByEmailIgnoreCase("fernando@gmail.com");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe lanzar EmailDuplicadoException si el email ya existe")
    void save_cuandoEmailExiste_deberiaLanzarException() {
        when(usuarioRepository.existsByEmailIgnoreCase("fernando@gmail.com")).thenReturn(true);

        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> usuarioService.save(usuario)
        );

        assertTrue(exception.getMessage().contains("fernando@gmail.com"));
        verify(usuarioRepository).existsByEmailIgnoreCase("fernando@gmail.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe buscar usuarios por nombre correctamente")
    void buscarPorNombre_deberiaRetornarLista() {
        when(usuarioRepository.findByNombreContainingIgnoreCase("Fernando")).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.buscarPorNombre("Fernando");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        verify(usuarioRepository).findByNombreContainingIgnoreCase("Fernando");
    }

    @Test
    @DisplayName("Debe buscar usuario por email correctamente")
    void buscarPorEmail_deberiaRetornarUsuario() {
        when(usuarioRepository.findByEmailIgnoreCase("fernando@gmail.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorEmail("fernando@gmail.com");

        assertNotNull(resultado);
        assertEquals("fernando@gmail.com", resultado.getEmail());

        verify(usuarioRepository).findByEmailIgnoreCase("fernando@gmail.com");
    }

    @Test
    @DisplayName("Debe buscar usuarios por rol correctamente")
    void buscarPorRol_deberiaRetornarLista() {
        when(usuarioRepository.findByRol(Rol.ADMIN)).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.buscarPorRol("ADMIN");

        assertNotNull(resultado);
        assertEquals(Rol.ADMIN, resultado.get(0).getRol());

        verify(usuarioRepository).findByRol(Rol.ADMIN);
    }

    @Test
    @DisplayName("Debe actualizar usuario correctamente manteniendo el mismo email")
    void update_mismoEmail_deberiaActualizarUsuario() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Fernando Actualizado");
        usuarioActualizado.setEmail("fernando@gmail.com");
        usuarioActualizado.setClave("abcd");
        usuarioActualizado.setRol(Rol.ADMIN);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.update(1L, usuarioActualizado);

        assertNotNull(resultado);
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe lanzar EmailDuplicadoException al actualizar con email de otro usuario")
    void update_emailDuplicado_deberiaLanzarException() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Fernando Actualizado");
        usuarioActualizado.setEmail("otro@gmail.com");
        usuarioActualizado.setClave("abcd");
        usuarioActualizado.setRol(Rol.ADMIN);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailIgnoreCase("otro@gmail.com")).thenReturn(true);

        assertThrows(
                EmailDuplicadoException.class,
                () -> usuarioService.update(1L, usuarioActualizado)
        );

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).existsByEmailIgnoreCase("otro@gmail.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe eliminar usuario por ID correctamente")
    void delete_deberiaEliminarUsuario() {
        usuarioService.delete(1L);

        verify(usuarioRepository).deleteById(1L);
    }
}