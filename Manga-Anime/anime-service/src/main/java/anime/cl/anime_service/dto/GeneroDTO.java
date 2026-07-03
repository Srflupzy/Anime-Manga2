package anime.cl.anime_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneroDTO {

    private Long id;
    private String nombre;
    private String descripcion;
}
