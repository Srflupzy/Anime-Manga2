package usuario.cl.usuario_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usuario.cl.usuario_service.Rol;
import usuario.cl.usuario_service.exception.EmailDuplicadoException;
import usuario.cl.usuario_service.model.Usuario;
import usuario.cl.usuario_service.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id){
        return usuarioRepository.findById(id).orElseThrow();
    }

    public Usuario save(Usuario usuario){
        usuario.setId(null);

        if (usuarioRepository
                .existsByEmailIgnoreCase(
                        usuario.getEmail().trim())) {
            throw new EmailDuplicadoException(
                    usuario.getEmail()
            );
        }


        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario){
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow();
        if (!usuarioExistente
                .getEmail()
                .equalsIgnoreCase(
                        usuario.getEmail()
                )
                &&
                usuarioRepository
                        .existsByEmailIgnoreCase(
                                usuario.getEmail()
                        )) {
            throw new EmailDuplicadoException(
                    usuario.getEmail()
            );
        }

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setClave(usuario.getClave());



        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Long id){
        usuarioRepository.deleteById(id);
    }


    public List<Usuario>
    buscarPorNombre(
            String nombre){

        return usuarioRepository
                .findByNombreContainingIgnoreCase(
                        nombre
                );
    }

    public Usuario
    buscarPorEmail(
            String email){

        return usuarioRepository
                .findByEmailIgnoreCase(
                        email
                )
                .orElseThrow();
    }
    public List<Usuario>
    buscarPorRol(
            String rol){

        Rol rolEnum =
                Rol.valueOf(
                        rol.toUpperCase()
                );

        return usuarioRepository
                .findByRol(
                        rolEnum
                );
    }

}
