package personaje.cl.personaje_service.controller;

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
import personaje.cl.personaje_service.dto.PersonajeDTO;
import personaje.cl.personaje_service.exception.ErrorResponse;
import personaje.cl.personaje_service.model.Personaje;
import personaje.cl.personaje_service.service.PersonajeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "Personajes",
        description = "Operaciones disponibles para la gestión de personajes"
)
@RestController
@RequestMapping("/api/v1/personajes")
@RequiredArgsConstructor
public class PersonajeController {

    private final PersonajeService personajeService;

    @Operation(summary = "Listar personajes", description = "Obtiene el listado completo de personajes con información del anime.")
    @ApiResponse(responseCode = "200", description = "Personajes listados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonajeDTO.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<PersonajeDTO>> listar() {
        return ResponseEntity.ok(personajeService.findAllConAnime());
    }

    @Operation(summary = "Buscar personaje por ID", description = "Obtiene un personaje específico según su identificador.")
    @ApiResponse(responseCode = "200", description = "Personaje encontrado correctamente", content = @Content(schema = @Schema(implementation = PersonajeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Personaje no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<PersonajeDTO> buscar(
            @Parameter(description = "ID del personaje", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                personajeService.findByIdConAnime(id)
        );
    }

    @Operation(summary = "Buscar personaje por nombre", description = "Busca personajes cuyo nombre coincida o contenga el texto ingresado.")
    @ApiResponse(responseCode = "200", description = "Personajes encontrados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Personaje.class))))
    @GetMapping("/buscar")
    public ResponseEntity<List<Personaje>> buscarPorNombre(
            @Parameter(description = "Nombre del personaje", example = "Naruto")
            @RequestParam String nombre) {

        return ResponseEntity.ok(
                personajeService.buscarPorNombre(nombre)
        );
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de personajes.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "personaje-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8096");

        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar personaje", description = "Registra un nuevo personaje. Solo puede ser realizado por un usuario con rol ADMIN.")
    @ApiResponse(responseCode = "201", description = "Personaje registrado correctamente", content = @Content(schema = @Schema(implementation = Personaje.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Personaje> guardar(
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @RequestParam String rol,
            @Valid @RequestBody Personaje personaje) {

        if (!rol.equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("No tienes permisos");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personajeService.save(personaje));
    }

    @Operation(summary = "Actualizar personaje", description = "Actualiza la información de un personaje existente. Solo puede ser realizado por un usuario con rol ADMIN.")
    @ApiResponse(responseCode = "200", description = "Personaje actualizado correctamente", content = @Content(schema = @Schema(implementation = Personaje.class)))
    @ApiResponse(responseCode = "404", description = "Personaje no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Personaje> actualizar(
            @Parameter(description = "ID del personaje", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @RequestParam String rol,
            @Valid @RequestBody Personaje personaje) {

        if (!rol.equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("No tienes permisos");

        return ResponseEntity.ok(
                personajeService.update(id, personaje)
        );
    }

    @Operation(summary = "Eliminar personaje", description = "Elimina un personaje existente. Solo puede ser realizado por un usuario con rol ADMIN.")
    @ApiResponse(responseCode = "204", description = "Personaje eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Personaje no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "No tienes permisos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del personaje", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @RequestParam String rol) {

        if (!rol.equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("No tienes permisos");

        personajeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}