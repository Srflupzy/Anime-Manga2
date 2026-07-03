package anime.cl.anime_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoDTO {

    private Long id;
    private Long usuarioId;
    private Long animeId;
}
