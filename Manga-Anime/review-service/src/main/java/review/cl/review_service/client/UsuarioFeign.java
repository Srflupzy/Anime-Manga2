package review.cl.review_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import review.cl.review_service.dto.UsuarioDTO;

@FeignClient(
        name = "usuario-service",
        url = "http://localhost:8099"
)
public interface UsuarioFeign {

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioDTO pedirUsuario(
            @PathVariable Long id
    );
}