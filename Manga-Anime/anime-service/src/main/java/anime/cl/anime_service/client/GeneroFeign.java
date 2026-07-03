package anime.cl.anime_service.client;

import anime.cl.anime_service.dto.GeneroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "genero-service",
        url = "http://localhost:8094"
)
public interface GeneroFeign {

    @GetMapping(
            "/api/v1/generos/{id}"
    )
    GeneroDTO obtenerGenero(
            @PathVariable Long id
    );
}
