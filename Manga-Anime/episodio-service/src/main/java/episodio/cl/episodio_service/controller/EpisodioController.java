package episodio.cl.episodio_service.controller;

import episodio.cl.episodio_service.exception.ErrorResponse;
import episodio.cl.episodio_service.model.Episodio;
import episodio.cl.episodio_service.service.EpisodioService;
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
        name = "Episodios",
        description = "Operaciones disponibles para la gestión de episodios"
)
@RestController
@RequestMapping("/api/v1/episodios")
@RequiredArgsConstructor
public class EpisodioController {

    private final EpisodioService episodioService;

    @Operation(
            summary = "Listar episodios",
            description = "Obtiene el listado completo de episodios registrados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Episodios listados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = Episodio.class)
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<Episodio>> listar() {

        return ResponseEntity.ok(
                episodioService.findAll()
        );
    }

    @Operation(
            summary = "Buscar episodio por ID",
            description = "Obtiene un episodio según su identificador."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Episodio encontrado correctamente",
            content = @Content(
                    schema = @Schema(implementation = Episodio.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Episodio no encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/{id}")
    public ResponseEntity<Episodio> buscar(

            @Parameter(
                    description = "ID del episodio",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                episodioService.findById(id)
        );
    }

    @Operation(
            summary = "Buscar episodio por título",
            description = "Busca episodios cuyo título contenga el texto ingresado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = Episodio.class)
                    )
            )
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<Episodio>> buscarPorTitulo(

            @Parameter(
                    description = "Título del episodio",
                    example = "Capítulo 1"
            )
            @RequestParam String titulo) {

        return ResponseEntity.ok(
                episodioService.buscarPorTitulo(
                        titulo
                )
        );
    }

    @Operation(
            summary = "Buscar episodios por temporada",
            description = "Obtiene todos los episodios pertenecientes a una temporada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Episodios encontrados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = Episodio.class)
                    )
            )
    )
    @GetMapping("/temporada/{temporadaId}")
    public ResponseEntity<List<Episodio>> buscarPorTemporada(

            @Parameter(
                    description = "ID de la temporada",
                    example = "1"
            )
            @PathVariable Long temporadaId) {

        return ResponseEntity.ok(
                episodioService.buscarPorTemporada(
                        temporadaId
                )
        );
    }

    @Operation(
            summary = "Obtener el episodio más largo",
            description = "Retorna el episodio con mayor duración registrado en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Episodio obtenido correctamente",
            content = @Content(
                    schema = @Schema(implementation = Episodio.class)
            )
    )
    @GetMapping("/mas-largo")
    public ResponseEntity<Episodio> episodioMasLargo() {

        return ResponseEntity.ok(
                episodioService.episodioMasLargo()
        );
    }    @Operation(
            summary = "Información del microservicio",
            description = "Obtiene información general del microservicio de episodios."
    )
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "episodio-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8091");

        return ResponseEntity.ok(informacion);
    }

    @Operation(
            summary = "Registrar episodio",
            description = "Registra un nuevo episodio. Solo puede ser realizado por un usuario con rol ADMIN."
    )
    @ApiResponse(responseCode = "201", description = "Episodio registrado correctamente", content = @Content(schema = @Schema(implementation = Episodio.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Episodio> guardar(
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @RequestParam String rol,
            @Valid @RequestBody Episodio episodio) {

        if (!rol.equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("No tienes permisos");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(episodioService.save(episodio));
    }

    @Operation(
            summary = "Actualizar episodio",
            description = "Actualiza la información de un episodio existente. Solo puede ser realizado por un usuario con rol ADMIN."
    )
    @ApiResponse(responseCode = "200", description = "Episodio actualizado correctamente", content = @Content(schema = @Schema(implementation = Episodio.class)))
    @ApiResponse(responseCode = "404", description = "Episodio no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Episodio> actualizar(
            @Parameter(description = "ID del episodio", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @RequestParam String rol,
            @Valid @RequestBody Episodio episodio) {

        if (!rol.equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("No tienes permisos");

        return ResponseEntity.ok(
                episodioService.update(id, episodio)
        );
    }

    @Operation(
            summary = "Eliminar episodio",
            description = "Elimina un episodio existente. Solo puede ser realizado por un usuario con rol ADMIN."
    )
    @ApiResponse(responseCode = "204", description = "Episodio eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Episodio no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "No tienes permisos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del episodio", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Rol del usuario", example = "ADMIN")
            @RequestParam String rol) {

        if (!rol.equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("No tienes permisos");

        episodioService.delete(id);

        return ResponseEntity.noContent().build();
    }
}