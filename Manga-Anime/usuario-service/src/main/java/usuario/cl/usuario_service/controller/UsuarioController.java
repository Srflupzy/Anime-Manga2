package usuario.cl.usuario_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.cl.usuario_service.exception.ErrorResponse;
import usuario.cl.usuario_service.model.Usuario;
import usuario.cl.usuario_service.service.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Usuarios", description = "Operaciones disponibles para la gestión de usuarios")
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios", description = "Obtiene el listado completo de usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Usuarios listados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Usuario.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario específico según su identificador.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente", content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Buscar usuarios por nombre", description = "Busca usuarios cuyo nombre coincida o contenga el texto ingresado.")
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Usuario.class))))
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorNombre(
            @Parameter(description = "Nombre del usuario", example = "Fernando")
            @RequestParam String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @Operation(summary = "Buscar usuario por email", description = "Obtiene un usuario según su correo electrónico.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente", content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(
            @Parameter(description = "Correo electrónico del usuario", example = "usuario@gmail.com")
            @PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @Operation(summary = "Buscar usuarios por rol", description = "Obtiene todos los usuarios asociados a un rol.")
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Usuario.class))))
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> buscarPorRol(
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(rol));
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de usuarios.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "usuario-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8099");

        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema.")
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente", content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Correo ya registrado o conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Usuario> guardar(
            @Valid @RequestBody Usuario usuario) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.save(usuario));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente", content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Correo ya registrado o conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.update(id, usuario));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}