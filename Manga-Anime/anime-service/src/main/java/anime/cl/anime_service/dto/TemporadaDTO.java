package anime.cl.anime_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporadaDTO {

    private Long id;
    private Integer numero;
    private Long animeId;
}