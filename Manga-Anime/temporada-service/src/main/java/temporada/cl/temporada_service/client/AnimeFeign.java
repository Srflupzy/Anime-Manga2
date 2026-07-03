package temporada.cl.temporada_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import temporada.cl.temporada_service.dto.AnimeDTO;

@FeignClient(
        name = "anime-service"
)
public interface AnimeFeign {

    @GetMapping("/api/v1/animes/{id}")
    AnimeDTO buscarAnime(
            @PathVariable Long id
    );
}
