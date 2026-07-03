package estudio.cl.estudio_service.mapper;

import estudio.cl.estudio_service.dto.EstudioDTO;
import estudio.cl.estudio_service.model.Estudio;
import org.springframework.stereotype.Component;

@Component
public class EstudioMapper {

    public EstudioDTO toDTO(Estudio estudio) {

        if (estudio == null) return null;

        EstudioDTO dto = new EstudioDTO();

        dto.setId(estudio.getId());
        dto.setNombre(estudio.getNombre());
        dto.setPais(estudio.getPais());

        return dto;
    }
}
