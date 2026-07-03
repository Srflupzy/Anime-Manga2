package favoritos.cl.favoritos_service.client;

import favoritos.cl.favoritos_service.dto.AnimeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "anime-service",
        url = "http://localhost:8090"
)
public interface AnimeFeign {

    @GetMapping("/api/v1/animes/{id}")
    AnimeDTO pedirAnime(
            @PathVariable Long id
    );
}
