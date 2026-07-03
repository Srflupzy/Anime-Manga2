package favoritos.cl.favoritos_service.mapper;

import favoritos.cl.favoritos_service.client.AnimeFeign;
import favoritos.cl.favoritos_service.client.UsuarioFeign;
import favoritos.cl.favoritos_service.dto.AnimeDTO;
import favoritos.cl.favoritos_service.dto.FavoritoDTO;
import favoritos.cl.favoritos_service.dto.UsuarioDTO;
import favoritos.cl.favoritos_service.model.Favorito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoritoMapper {

    @Autowired
    private UsuarioFeign usuarioFeign;

    @Autowired
    private AnimeFeign animeFeign;

    public FavoritoDTO toDTO(
            Favorito favorito) {
        if (favorito == null)
            return null;
        FavoritoDTO dto =
                new FavoritoDTO();
        dto.setId(favorito.getId());
        try {
            dto.setUsuario(
                    usuarioFeign.pedirUsuario(favorito.getUsuarioId()).getNombre());
        } catch (Exception e) {
            dto.setUsuario(null);
        }
        try {
            dto.setAnime(animeFeign.pedirAnime(favorito.getAnimeId()).getTitulo());
        } catch (Exception e) {
            dto.setAnime(null);
        }
        return dto;
    }
}
