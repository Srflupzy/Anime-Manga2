package usuario.cl.usuario_service.mapper;

import org.springframework.stereotype.Component;
import usuario.cl.usuario_service.dto.UsuarioDTO;
import usuario.cl.usuario_service.model.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {

        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());

        return dto;
    }
}
