package anime.cl.anime_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeInputDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotBlank(message = "El estudio no puede estar vacío")
    private String estudio;

    @NotNull(message = "El número de temporadas es obligatorio")
    @Min(value = 1, message = "El anime debe tener al menos 1 temporada")
    private Integer temporadas;

    @NotNull(message = "El ID del género es obligatorio")
    private Long generoId;
}
