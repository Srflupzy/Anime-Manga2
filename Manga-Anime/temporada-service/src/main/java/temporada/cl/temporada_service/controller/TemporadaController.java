package temporada.cl.temporada_service.controller;

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
import temporada.cl.temporada_service.dto.TemporadaDTO;
import temporada.cl.temporada_service.exception.ErrorResponse;
import temporada.cl.temporada_service.model.Temporada;
import temporada.cl.temporada_service.service.TemporadaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Temporadas", description = "Operaciones disponibles para la gestión de temporadas")
@RestController
@RequestMapping("/api/v1/temporadas")
@RequiredArgsConstructor
public class TemporadaController {

    private final TemporadaService temporadaService;

    @Operation(summary = "Listar temporadas", description = "Obtiene el listado completo de temporadas con el nombre del anime.")
    @ApiResponse(responseCode = "200", description = "Temporadas listadas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TemporadaDTO.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<TemporadaDTO>> listar() {
        return ResponseEntity.ok(temporadaService.findAllConNombre());
    }

    @Operation(summary = "Buscar temporada por ID", description = "Obtiene una temporada específica según su identificador.")
    @ApiResponse(responseCode = "200", description = "Temporada encontrada correctamente", content = @Content(schema = @Schema(implementation = TemporadaDTO.class)))
    @ApiResponse(responseCode = "404", description = "Temporada no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<TemporadaDTO> buscar(
            @Parameter(description = "ID de la temporada", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(temporadaService.findByIdConAnime(id));
    }

    @Operation(summary = "Buscar temporadas por número", description = "Busca temporadas según el número ingresado.")
    @ApiResponse(responseCode = "200", description = "Temporadas encontradas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Temporada.class))))
    @GetMapping("/buscar")
    public ResponseEntity<List<Temporada>> buscarPorNumero(
            @Parameter(description = "Número de temporada", example = "1")
            @RequestParam Integer numero) {
        return ResponseEntity.ok(temporadaService.buscarPorNumero(numero));
    }

    @Operation(summary = "Buscar temporadas por anime", description = "Obtiene las temporadas asociadas a un anime.")
    @ApiResponse(responseCode = "200", description = "Temporadas encontradas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TemporadaDTO.class))))
    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<TemporadaDTO>> buscarPorAnime(
            @Parameter(description = "ID del anime", example = "1")
            @PathVariable Long animeId) {
        return ResponseEntity.ok(temporadaService.buscarPorAnimeConNombre(animeId));
    }

    @Operation(summary = "Buscar temporada específica", description = "Obtiene una temporada específica por anime y número de temporada.")
    @ApiResponse(responseCode = "200", description = "Temporada encontrada correctamente", content = @Content(schema = @Schema(implementation = Temporada.class)))
    @ApiResponse(responseCode = "404", description = "Temporada no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/anime/{animeId}/numero/{numero}")
    public ResponseEntity<Temporada> buscarTemporadaEspecifica(
            @Parameter(description = "ID del anime", example = "1")
            @PathVariable Long animeId,
            @Parameter(description = "Número de temporada", example = "2")
            @PathVariable Integer numero) {
        return ResponseEntity.ok(temporadaService.buscarTemporadaEspecifica(animeId, numero));
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de temporadas.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "temporada-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8091");
        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar temporada", description = "Registra una nueva temporada en el sistema.")
    @ApiResponse(responseCode = "201", description = "Temporada registrada correctamente", content = @Content(schema = @Schema(implementation = Temporada.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Temporada> guardar(@Valid @RequestBody Temporada temporada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temporadaService.save(temporada));
    }

    @Operation(summary = "Actualizar temporada", description = "Actualiza la información de una temporada existente.")
    @ApiResponse(responseCode = "200", description = "Temporada actualizada correctamente", content = @Content(schema = @Schema(implementation = Temporada.class)))
    @ApiResponse(responseCode = "404", description = "Temporada no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Temporada> actualizar(
            @Parameter(description = "ID de la temporada", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Temporada temporada) {
        return ResponseEntity.ok(temporadaService.update(id, temporada));
    }

    @Operation(summary = "Eliminar temporada", description = "Elimina una temporada existente.")
    @ApiResponse(responseCode = "204", description = "Temporada eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Temporada no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la temporada", example = "1")
            @PathVariable Long id) {
        temporadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}