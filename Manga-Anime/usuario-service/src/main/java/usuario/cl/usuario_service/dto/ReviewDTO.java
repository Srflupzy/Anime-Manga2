package usuario.cl.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;
    private String comentario;
    private Integer puntuacion;
    private Long usuarioId;
    private Long animeId;
}