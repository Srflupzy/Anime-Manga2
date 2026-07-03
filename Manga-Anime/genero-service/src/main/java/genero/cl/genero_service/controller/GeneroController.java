package genero.cl.genero_service.controller;

import genero.cl.genero_service.exception.ErrorResponse;
import genero.cl.genero_service.model.Genero;
import genero.cl.genero_service.service.GeneroService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "Géneros",
        description = "Operaciones disponibles para la gestión de géneros"
)
@RestController
@RequestMapping("/api/v1/generos")
@RequiredArgsConstructor
public class GeneroController {

    private final GeneroService generoService;

    @Operation(summary = "Listar géneros", description = "Obtiene el listado completo de géneros registrados.")
    @ApiResponse(responseCode = "200", description = "Géneros listados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Genero.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<Genero>> listar() {
        return ResponseEntity.ok(generoService.findAll());
    }

    @Operation(summary = "Buscar género por ID", description = "Obtiene un género según su identificador.")
    @ApiResponse(responseCode = "200", description = "Género encontrado correctamente", content = @Content(schema = @Schema(implementation = Genero.class)))
    @ApiResponse(responseCode = "404", description = "Género no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Genero> buscar(
            @Parameter(description = "ID del género", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                generoService.findById(id)
        );
    }

    @Operation(summary = "Buscar género por nombre", description = "Busca géneros cuyo nombre coincida o contenga el texto ingresado.")
    @ApiResponse(responseCode = "200", description = "Géneros encontrados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Genero.class))))
    @GetMapping("/buscar")
    public ResponseEntity<List<Genero>> buscarPorNombre(
            @Parameter(description = "Nombre del género", example = "Shonen")
            @RequestParam String nombre) {

        return ResponseEntity.ok(
                generoService.buscarPorNombre(nombre)
        );
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de géneros.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "genero-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8094");

        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar género", description = "Registra un nuevo género en el sistema.")
    @ApiResponse(responseCode = "201", description = "Género registrado correctamente", content = @Content(schema = @Schema(implementation = Genero.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Genero> guardar(
            @Valid @RequestBody Genero genero) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generoService.save(genero));
    }

    @Operation(summary = "Actualizar género", description = "Actualiza la información de un género existente.")
    @ApiResponse(responseCode = "200", description = "Género actualizado correctamente", content = @Content(schema = @Schema(implementation = Genero.class)))
    @ApiResponse(responseCode = "404", description = "Género no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Genero> actualizar(
            @Parameter(description = "ID del género", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Genero genero) {

        return ResponseEntity.ok(
                generoService.update(id, genero)
        );
    }

    @Operation(summary = "Eliminar género", description = "Elimina un género existente.")
    @ApiResponse(responseCode = "204", description = "Género eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Género no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del género", example = "1")
            @PathVariable Long id) {

        generoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}