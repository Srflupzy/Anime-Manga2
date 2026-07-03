package personaje.cl.personaje_service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import personaje.cl.personaje_service.client.AnimeFeign;
import personaje.cl.personaje_service.dto.PersonajeDTO;
import personaje.cl.personaje_service.model.Personaje;

@Component
public class PersonajeMapper {

    @Autowired
    private AnimeFeign animeFeign;

    public PersonajeDTO toDTO(
            Personaje personaje) {if (personaje == null) return null;

        PersonajeDTO dto = new PersonajeDTO();

        dto.setId(personaje.getId());
        dto.setNombre(personaje.getNombre());
        dto.setRol(personaje.getRol());
        try {
            dto.setAnimeNombre(animeFeign.pedirAnime(personaje.getAnimeId()).getTitulo());
        } catch (Exception e) {
            dto.setAnimeNombre(null);
        }
        return dto;
    }
}
