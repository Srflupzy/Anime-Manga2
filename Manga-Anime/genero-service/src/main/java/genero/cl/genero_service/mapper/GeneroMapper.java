package genero.cl.genero_service.mapper;

import genero.cl.genero_service.dto.GeneroDTO;
import genero.cl.genero_service.model.Genero;
import org.springframework.stereotype.Component;

@Component
public class GeneroMapper {

    public GeneroDTO toDTO(Genero genero) {

        if (genero == null) return null;

        GeneroDTO dto = new GeneroDTO();

        dto.setId(genero.getId());
        dto.setNombre(genero.getNombre());
        dto.setDescripcion(genero.getDescripcion());

        return dto;
    }
}