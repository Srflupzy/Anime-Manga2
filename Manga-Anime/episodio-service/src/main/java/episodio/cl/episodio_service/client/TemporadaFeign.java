package episodio.cl.episodio_service.client;

import episodio.cl.episodio_service.dto.TemporadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "temporada-service",
        url = "http://localhost:8098"
)
public interface TemporadaFeign {

    @GetMapping("/api/v1/animes/{id}")
    TemporadaDTO pedirTemporada(
            @PathVariable Long id
    );
}
