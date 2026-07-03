package review.cl.review_service.controller;

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
import review.cl.review_service.dto.ReviewDTO;
import review.cl.review_service.exception.ErrorResponse;
import review.cl.review_service.model.Review;
import review.cl.review_service.service.ReviewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Reviews", description = "Operaciones disponibles para la gestión de reviews")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Listar reviews", description = "Obtiene el listado completo de reviews con usuario y anime.")
    @ApiResponse(responseCode = "200", description = "Reviews listadas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReviewDTO.class))))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> listar() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @Operation(summary = "Buscar review por ID", description = "Obtiene una review específica según su identificador.")
    @ApiResponse(responseCode = "200", description = "Review encontrada correctamente", content = @Content(schema = @Schema(implementation = ReviewDTO.class)))
    @ApiResponse(responseCode = "404", description = "Review no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> buscar(
            @Parameter(description = "ID de la review", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @Operation(summary = "Buscar reviews por anime", description = "Obtiene todas las reviews asociadas a un anime.")
    @ApiResponse(responseCode = "200", description = "Reviews encontradas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Review.class))))
    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<Review>> buscarPorAnime(
            @Parameter(description = "ID del anime", example = "1")
            @PathVariable Long animeId) {
        return ResponseEntity.ok(reviewService.buscarPorAnime(animeId));
    }

    @Operation(summary = "Buscar reviews por usuario", description = "Obtiene todas las reviews realizadas por un usuario.")
    @ApiResponse(responseCode = "200", description = "Reviews encontradas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Review.class))))
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Review>> buscarPorUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(reviewService.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Listar reviews destacadas", description = "Obtiene las reviews con puntuación máxima.")
    @ApiResponse(responseCode = "200", description = "Reviews destacadas obtenidas correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Review.class))))
    @GetMapping("/top")
    public ResponseEntity<List<Review>> topReviews() {
        return ResponseEntity.ok(reviewService.topReviews());
    }

    @Operation(summary = "Promedio de puntuación por anime", description = "Calcula el promedio de puntuación de las reviews asociadas a un anime.")
    @ApiResponse(responseCode = "200", description = "Promedio calculado correctamente")
    @GetMapping("/promedio/{animeId}")
    public ResponseEntity<Double> promedioAnime(
            @Parameter(description = "ID del anime", example = "1")
            @PathVariable Long animeId) {
        return ResponseEntity.ok(reviewService.promedioAnime(animeId));
    }

    @Operation(summary = "Información del microservicio", description = "Obtiene información general del microservicio de reviews.")
    @ApiResponse(responseCode = "200", description = "Información obtenida correctamente")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> informacion = new HashMap<>();
        informacion.put("microservicio", "review-service");
        informacion.put("estado", "activo");
        informacion.put("version", "1.0");
        informacion.put("puerto", "8097");
        return ResponseEntity.ok(informacion);
    }

    @Operation(summary = "Registrar review", description = "Registra una nueva review en el sistema.")
    @ApiResponse(responseCode = "201", description = "Review registrada correctamente", content = @Content(schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto de negocio", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Review> guardar(@Valid @RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.save(review));
    }

    @Operation(summary = "Actualizar review", description = "Actualiza la información de una review existente.")
    @ApiResponse(responseCode = "200", description = "Review actualizada correctamente", content = @Content(schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "404", description = "Review no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Review> actualizar(
            @Parameter(description = "ID de la review", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.update(id, review));
    }

    @Operation(summary = "Eliminar review", description = "Elimina una review existente.")
    @ApiResponse(responseCode = "204", description = "Review eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Review no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la review", example = "1")
            @PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}