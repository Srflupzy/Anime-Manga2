package favoritos.cl.favoritos_service.client;

import favoritos.cl.favoritos_service.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
