package episodio.cl.episodio_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioDTO {

    private Long id;
    private String titulo;
    private Integer duracion;
    private Long temporadaId;
}