package estudio.cl.estudio_service.controller;

import estudio.cl.estudio_service.exception.ErrorResponse;
import estudio.cl.estudio_service.model.Estudio;
import estudio.cl.estudio_service.service.EstudioService;
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
        name = "Estudios",
        description = "Operaciones disponibles para la gestión de estudios de animación"
)
@RestController
@RequestMapping("/api/v1/estudios")
@RequiredArgsConstructor
public class EstudioController {

    private final EstudioService estudioService;

    @Operation(summary = "Listar estudios", description = "Obtiene el listado completo de estudios registrados.")
    @ApiResponse(responseCode = "200", description = "Estudios listados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Estudio.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<Estudio>> listar() {
        return ResponseEntity.ok(estudioService.findAll());
    }

    @Operation(summary = "Buscar estudio por ID", description = "Obtiene un estudio según su identificador.")
    @ApiResponse(responseCode = "200", description = "Estudio encontrado correctamente", content = @Content(schema = @Schema(implementation = Estudio.class)))
    @ApiResponse(responseCode = "404", description = "Estudio no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Estudio> buscar(
            @Parameter(description = "ID del estudio", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(estudioService.findById(id));
    }

    @Operation(summary = "Buscar estudio por nombre", description = "Busca un estudio según su nombre.")
    @ApiResponse(responseCode = "200", description = "Estudio encontrado correctamente", content = @Content(schema = @Schema(implementation = Estudio.class)))
    @ApiResponse(responseCode = "404", description = "Estudio no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/buscar")
    public ResponseEntity<Estudio> buscarPorNombre(
            @Parameter(description = "Nombre del estudio", example = "Ufotable")
            @RequestParam String nombre) {

        return ResponseEntity.ok(estudioService.buscarPorNombre(nombre));
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de estudios.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "estudio-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8093");

        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar estudio", description = "Registra un nuevo estudio de animación.")
    @ApiResponse(responseCode = "201", description = "Estudio registrado correctamente", content = @Content(schema = @Schema(implementation = Estudio.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Estudio> guardar(@Valid @RequestBody Estudio estudio) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estudioService.save(estudio));
    }

    @Operation(summary = "Actualizar estudio", description = "Actualiza la información de un estudio existente.")
    @ApiResponse(responseCode = "200", description = "Estudio actualizado correctamente", content = @Content(schema = @Schema(implementation = Estudio.class)))
    @ApiResponse(responseCode = "404", description = "Estudio no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Estudio> actualizar(
            @Parameter(description = "ID del estudio", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Estudio estudio) {

        return ResponseEntity.ok(estudioService.update(id, estudio));
    }

    @Operation(summary = "Eliminar estudio", description = "Elimina un estudio existente.")
    @ApiResponse(responseCode = "204", description = "Estudio eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Estudio no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del estudio", example = "1")
            @PathVariable Long id) {

        estudioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}