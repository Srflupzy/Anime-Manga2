package manga.cl.manga_service.mapper;

import manga.cl.manga_service.client.GeneroFeign;
import manga.cl.manga_service.dto.MangaDTO;
import manga.cl.manga_service.model.Manga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MangaMapper {

    @Autowired
    private GeneroFeign generoFeign;

    public MangaDTO toDTO(Manga manga) {

        if (manga == null) return null;

        MangaDTO dto = new MangaDTO();

        dto.setId(manga.getId());
        dto.setTitulo(manga.getTitulo());
        dto.setAutor(manga.getAutor());
        dto.setTomos(manga.getTomos());

        try {

            dto.setGenero(
                    generoFeign
                            .pedirGenero(
                                    manga.getGeneroId()
                            )
                            .getNombre()
            );

        } catch (Exception e) {

            dto.setGenero(null);
        }

        return dto;
    }
}
