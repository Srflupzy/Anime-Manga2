package anime.cl.anime_service.client;

import anime.cl.anime_service.dto.EstudioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "estudio-service")
public interface EstudioFeing {

    @GetMapping("/api/v1/estudios/buscar")
    EstudioDTO obtenerEstudio(@RequestParam String nombre);
}