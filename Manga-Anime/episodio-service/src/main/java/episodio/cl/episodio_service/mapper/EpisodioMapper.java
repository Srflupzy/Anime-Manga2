package episodio.cl.episodio_service.mapper;

import episodio.cl.episodio_service.dto.EpisodioDTO;
import episodio.cl.episodio_service.model.Episodio;
import org.springframework.stereotype.Component;

@Component
public class EpisodioMapper {

    public EpisodioDTO toDTO(Episodio episodio) {

        if (episodio == null) return null;

        EpisodioDTO dto = new EpisodioDTO();

        dto.setId(episodio.getId());
        dto.setTitulo(episodio.getTitulo());
        dto.setDuracion(episodio.getDuracion());
        dto.setTemporadaId(episodio.getTemporadaId());

        return dto;
    }
}
