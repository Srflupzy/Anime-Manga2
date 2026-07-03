package manga.cl.manga_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manga.cl.manga_service.dto.MangaDTO;
import manga.cl.manga_service.exception.ErrorResponse;
import manga.cl.manga_service.model.Manga;
import manga.cl.manga_service.service.MangaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Tag(name = "Mangas", description = "Operaciones disponibles para la gestión de mangas")
@RestController
@RequestMapping("/api/v1/mangas")
@RequiredArgsConstructor
public class MangaController {

    private final MangaService mangaService;

    @Operation(summary = "Listar mangas", description = "Obtiene el listado completo de mangas con el nombre del género.")
    @ApiResponse(responseCode = "200", description = "Mangas listados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MangaDTO.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<MangaDTO>> listar() {
        return ResponseEntity.ok(mangaService.findAllConGenero());
    }

    @Operation(summary = "Buscar manga por ID", description = "Obtiene un manga específico según su identificador.")
    @ApiResponse(responseCode = "200", description = "Manga encontrado correctamente", content = @Content(schema = @Schema(implementation = MangaDTO.class)))
    @ApiResponse(responseCode = "404", description = "Manga no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<MangaDTO> buscar(
            @Parameter(description = "ID del manga", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(mangaService.findByIdConGenero(id));
    }

    @Operation(summary = "Buscar manga por título", description = "Busca mangas cuyo título contenga el texto ingresado.")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Manga.class))))
    @GetMapping("/buscar")
    public ResponseEntity<List<Manga>> buscarPorTitulo(
            @Parameter(description = "Título o parte del título del manga", example = "Berserk")
            @RequestParam String titulo) {
        return ResponseEntity.ok(mangaService.buscarPorTitulo(titulo));
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de mangas.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "manga-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8095");
        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar manga", description = "Registra un nuevo manga en el sistema.")
    @ApiResponse(responseCode = "201", description = "Manga registrado correctamente", content = @Content(schema = @Schema(implementation = Manga.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Manga> guardar(@Valid @RequestBody Manga manga) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mangaService.save(manga));
    }

    @Operation(summary = "Actualizar manga", description = "Actualiza la información de un manga existente.")
    @ApiResponse(responseCode = "200", description = "Manga actualizado correctamente", content = @Content(schema = @Schema(implementation = Manga.class)))
    @ApiResponse(responseCode = "404", description = "Manga no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Manga> actualizar(
            @Parameter(description = "ID del manga", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Manga manga) {
        return ResponseEntity.ok(mangaService.update(id, manga));
    }

    @Operation(summary = "Eliminar manga", description = "Elimina un manga existente.")
    @ApiResponse(responseCode = "204", description = "Manga eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Manga no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del manga", example = "1")
            @PathVariable Long id) {
        mangaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}