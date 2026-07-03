package anime.cl.anime_service.controller;

import anime.cl.anime_service.dto.AnimeDTO;
import anime.cl.anime_service.exception.ErrorResponse;
import anime.cl.anime_service.model.Anime;
import anime.cl.anime_service.service.AnimeService;
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
        name = "Animes",
        description = "Operaciones disponibles para la gestión de animes"
)
@RestController
@RequestMapping("/api/v1/animes")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @Operation(
            summary = "Listar animes",
            description = "Obtiene el listado completo de animes registrados en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Animes listados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = AnimeDTO.class)
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
    public ResponseEntity<List<AnimeDTO>> listar() {

        return ResponseEntity.ok(
                animeService.findAll()
        );
    }

    @Operation(
            summary = "Buscar anime por ID",
            description = "Obtiene un anime específico según su identificador."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Anime encontrado correctamente",
            content = @Content(
                    schema = @Schema(implementation = AnimeDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Anime no encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnimeDTO> buscar(

            @Parameter(
                    description = "ID del anime",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                animeService.findById(id)
        );
    }

    @Operation(
            summary = "Buscar anime por título",
            description = "Busca uno o más animes cuyo título contenga el texto ingresado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = AnimeDTO.class)
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
    @GetMapping("/buscar")
    public ResponseEntity<List<AnimeDTO>> buscarPorTitulo(

            @Parameter(
                    description = "Título o parte del título del anime",
                    example = "Naruto"
            )
            @RequestParam String titulo) {

        return ResponseEntity.ok(
                animeService.buscarPorTitulo(titulo)
        );
    }
    @Operation(
            summary = "Información del microservicio",
            description = "Obtiene información general del microservicio de animes."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Información obtenida correctamente"
    )
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {

        Map<String, String> informacion = new HashMap<>();

        informacion.put(
                "microservicio",
                "anime-service"
        );

        informacion.put(
                "estado",
                "activo"
        );

        informacion.put(
                "version",
                "1.0"
        );

        informacion.put(
                "puerto",
                "8090"
        );

        return ResponseEntity.ok(
                informacion
        );
    }

    @Operation(
            summary = "Registrar anime",
            description = "Registra un nuevo anime en el sistema. Solo puede ser realizado por un usuario con rol ADMIN."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Anime registrado correctamente",
            content = @Content(
                    schema = @Schema(implementation = Anime.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflicto de negocio (anime duplicado, estudio o género inexistente)",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping
    public ResponseEntity<Anime> guardar(

            @Parameter(
                    description = "Rol del usuario",
                    example = "ADMIN"
            )
            @RequestParam String rol,

            @Valid
            @RequestBody Anime anime) {

        if (!rol.equalsIgnoreCase("ADMIN")) {

            throw new RuntimeException(
                    "No tienes permisos"
            );
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        animeService.save(anime)
                );
    }

    @Operation(
            summary = "Actualizar anime",
            description = "Actualiza la información de un anime existente."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Anime actualizado correctamente",
            content = @Content(
                    schema = @Schema(implementation = Anime.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Anime no encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflicto de negocio",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<Anime> actualizar(

            @Parameter(
                    description = "ID del anime",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid
            @RequestBody Anime anime) {

        return ResponseEntity.ok(
                animeService.update(id, anime)
        );
    }

    @Operation(
            summary = "Eliminar anime",
            description = "Elimina un anime existente. Solo puede ser realizado por un usuario con rol ADMIN."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Anime eliminado correctamente"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Anime no encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "No tienes permisos",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(

            @Parameter(
                    description = "ID del anime",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Rol del usuario",
                    example = "ADMIN"
            )
            @RequestParam String rol) {

        if (!rol.equalsIgnoreCase("ADMIN")) {

            throw new RuntimeException(
                    "No tienes permisos"
            );
        }

        animeService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}