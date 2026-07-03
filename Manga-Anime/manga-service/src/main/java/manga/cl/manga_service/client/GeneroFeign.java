package manga.cl.manga_service.client;


import manga.cl.manga_service.dto.GeneroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "genero-service",
        url = "http://localhost:8094"
)
public interface GeneroFeign {

    @GetMapping("/api/v1/generos/{id}")
    GeneroDTO pedirGenero(
            @PathVariable Long id
    );
}