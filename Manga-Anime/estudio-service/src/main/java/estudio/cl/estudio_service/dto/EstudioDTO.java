package estudio.cl.estudio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioDTO {

    private Long id;
    private String nombre;
    private String pais;
}
