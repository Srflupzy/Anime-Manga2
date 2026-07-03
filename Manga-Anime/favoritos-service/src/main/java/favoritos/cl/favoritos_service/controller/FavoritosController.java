package favoritos.cl.favoritos_service.controller;

import favoritos.cl.favoritos_service.dto.FavoritoDTO;
import favoritos.cl.favoritos_service.exception.ErrorResponse;
import favoritos.cl.favoritos_service.model.Favorito;
import favoritos.cl.favoritos_service.service.FavoritosService;
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
        name = "Favoritos",
        description = "Operaciones disponibles para la gestión de favoritos"
)
@RestController
@RequestMapping("/api/v1/favoritos")
@RequiredArgsConstructor
public class FavoritosController {

    private final FavoritosService favoritoService;

    @Operation(summary = "Listar favoritos", description = "Obtiene el listado completo de favoritos con información detallada.")
    @ApiResponse(responseCode = "200", description = "Favoritos listados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FavoritoDTO.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<FavoritoDTO>> listar() {
        return ResponseEntity.ok(favoritoService.findAllConDatos());
    }

    @Operation(summary = "Buscar favorito por ID", description = "Obtiene un favorito específico según su identificador.")
    @ApiResponse(responseCode = "200", description = "Favorito encontrado correctamente", content = @Content(schema = @Schema(implementation = FavoritoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Favorito no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<FavoritoDTO> buscar(
            @Parameter(description = "ID del favorito", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                favoritoService.findByIdConDatos(id)
        );
    }

    @Operation(summary = "Buscar favoritos por usuario", description = "Obtiene los favoritos asociados a un usuario.")
    @ApiResponse(responseCode = "200", description = "Favoritos encontrados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FavoritoDTO.class))))
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FavoritoDTO>> buscarPorUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                favoritoService.findAllConDatos()
        );
    }

    @Operation(summary = "Buscar favoritos por anime", description = "Obtiene todos los favoritos asociados a un anime.")
    @ApiResponse(responseCode = "200", description = "Favoritos encontrados correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Favorito.class))))
    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<Favorito>> buscarPorAnime(
            @Parameter(description = "ID del anime", example = "1")
            @PathVariable Long animeId) {

        return ResponseEntity.ok(
                favoritoService.buscarPorAnime(animeId)
        );
    }

    @Operation(summary = "Contar favoritos", description = "Obtiene la cantidad total de favoritos asociados a un anime.")
    @ApiResponse(responseCode = "200", description = "Conteo obtenido correctamente")
    @GetMapping("/conteo/{animeId}")
    public ResponseEntity<Long> conteoFavoritos(
            @Parameter(description = "ID del anime", example = "1")
            @PathVariable Long animeId) {

        return ResponseEntity.ok(
                favoritoService.conteoFavoritos(animeId).longValue()
        );
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de favoritos.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "favoritos-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8093");

        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar favorito", description = "Registra un nuevo favorito en el sistema.")
    @ApiResponse(responseCode = "201", description = "Favorito registrado correctamente", content = @Content(schema = @Schema(implementation = Favorito.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Favorito> guardar(
            @Valid @RequestBody Favorito favorito) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoritoService.save(favorito));
    }

    @Operation(summary = "Actualizar favorito", description = "Actualiza la información de un favorito existente.")
    @ApiResponse(responseCode = "200", description = "Favorito actualizado correctamente", content = @Content(schema = @Schema(implementation = Favorito.class)))
    @ApiResponse(responseCode = "404", description = "Favorito no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Favorito> actualizar(
            @Parameter(description = "ID del favorito", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Favorito favorito) {

        return ResponseEntity.ok(
                favoritoService.update(id, favorito)
        );
    }

    @Operation(summary = "Eliminar favorito", description = "Elimina un favorito existente.")
    @ApiResponse(responseCode = "204", description = "Favorito eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Favorito no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del favorito", example = "1")
            @PathVariable Long id) {

        favoritoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}