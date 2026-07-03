package usuario.cl.usuario_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import usuario.cl.usuario_service.controller.UsuarioController;
import usuario.cl.usuario_service.model.Usuario;
import usuario.cl.usuario_service.service.UsuarioService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pruebas en la capa Controller de Usuarios")
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Fernando");
        usuario.setEmail("fernando@gmail.com");
        usuario.setClave("1234");
        usuario.setRol(Rol.ADMIN);
    }

    @Test
    @DisplayName("GET /api/v1/usuarios - Debería retornar 200 OK")
    void testListar() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Fernando"))
                .andExpect(jsonPath("$[0].email").value("fernando@gmail.com"));
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/{id} - Debería retornar 200 OK")
    void testBuscarPorId() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Fernando"))
                .andExpect(jsonPath("$.email").value("fernando@gmail.com"));
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/buscar - Debería retornar 200 OK")
    void testBuscarPorNombre() throws Exception {
        when(usuarioService.buscarPorNombre("Fernando")).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/buscar").param("nombre", "Fernando"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Fernando"));
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/email/{email} - Debería retornar 200 OK")
    void testBuscarPorEmail() throws Exception {
        when(usuarioService.buscarPorEmail("fernando@gmail.com")).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/email/fernando@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("fernando@gmail.com"));
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/rol/{rol} - Debería retornar 200 OK")
    void testBuscarPorRol() throws Exception {
        when(usuarioService.buscarPorRol("ADMIN")).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/rol/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rol").value("ADMIN"));
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/info - Debería retornar 200 OK")
    void testInfo() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microservicio").value("usuario-service"));
    }

    @Test
    @DisplayName("POST /api/v1/usuarios - Debería retornar 201 CREATED")
    void testGuardar() throws Exception {
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        String json = """
            {
              "nombre": "Fernando",
              "email": "fernando@gmail.com",
              "clave": "12345678",
              "rol": "ADMIN"
            }
            """;

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    @DisplayName("PUT /api/v1/usuarios/{id} - Debería retornar 200 OK")
    void testActualizar() throws Exception {
        when(usuarioService.update(eq(1L), any(Usuario.class))).thenReturn(usuario);

        String json = """
            {
              "nombre": "Fernando",
              "email": "fernando@gmail.com",
              "clave": "12345678",
              "rol": "ADMIN"
            }
            """;

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} - Debería retornar 204 NO CONTENT")
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}