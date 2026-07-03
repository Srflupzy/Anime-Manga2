package genero.cl.genero_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String estudio;
    private Integer temporadas;
    private Long generoId;
}