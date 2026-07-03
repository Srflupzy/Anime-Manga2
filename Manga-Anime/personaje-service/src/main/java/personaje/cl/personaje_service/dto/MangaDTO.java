package personaje.cl.personaje_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MangaDTO {

    private Long id;
    private String titulo;
    private String autor;
    private Integer tomos;
    private Long generoId;
}