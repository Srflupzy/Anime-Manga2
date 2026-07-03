package temporada.cl.temporada_service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import temporada.cl.temporada_service.client.AnimeFeign;
import temporada.cl.temporada_service.dto.AnimeDTO;
import temporada.cl.temporada_service.dto.TemporadaDTO;
import temporada.cl.temporada_service.model.Temporada;

@Component
public class TemporadaMapper {

    @Autowired
    private AnimeFeign animeFeign;
    public TemporadaDTO toDTO(Temporada temporada) {

        if (temporada == null) return null;

        TemporadaDTO dto = new TemporadaDTO();

        dto.setId(temporada.getId());
        dto.setNumeroTemporada(temporada.getNumero());
        try {

            AnimeDTO anime =
                    animeFeign.buscarAnime(
                            temporada.getAnimeId()
                    );

            dto.setAnimeNombre(
                    anime.getTitulo()
            );

        } catch (Exception e) {

            dto.setAnimeNombre(null);

        }

        return dto;
    }
}
