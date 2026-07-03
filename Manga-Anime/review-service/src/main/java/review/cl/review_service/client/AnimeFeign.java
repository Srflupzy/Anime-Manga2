package review.cl.review_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import review.cl.review_service.dto.AnimeDTO;

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
