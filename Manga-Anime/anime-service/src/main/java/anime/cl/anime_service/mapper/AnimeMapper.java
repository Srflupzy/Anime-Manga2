package anime.cl.anime_service.mapper;


import anime.cl.anime_service.client.GeneroFeign;
import anime.cl.anime_service.dto.AnimeDTO;
import anime.cl.anime_service.model.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


    @Component
    @RequiredArgsConstructor
    public class AnimeMapper {

        private final GeneroFeign generoFeign;

        public AnimeDTO
        toDTO(Anime anime) {

            if (anime == null)
                return null;

            String generoNombre;

            try {

                generoNombre =
                        generoFeign
                                .obtenerGenero(
                                        anime.getGeneroId()
                                )
                                .getNombre();

            } catch (Exception e) {

                generoNombre = null;
            }

            return new AnimeDTO(
                    anime.getId(),
                    anime.getTitulo(),
                    anime.getDescripcion(),
                    anime.getEstudio(),
                    anime.getTemporadas(),
                    generoNombre
            );
        }

}
