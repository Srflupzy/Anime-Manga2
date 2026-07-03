package favoritos.cl.favoritos_service.service;

import favoritos.cl.favoritos_service.dto.FavoritoDTO;
import favoritos.cl.favoritos_service.exception.FavoritosNoEncontradoException;
import favoritos.cl.favoritos_service.mapper.FavoritoMapper;
import favoritos.cl.favoritos_service.model.Favorito;
import favoritos.cl.favoritos_service.repository.FavoritosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritosService {

    private final FavoritosRepository favoritosRepository;
    private final FavoritoMapper favoritoMapper;

    public List<FavoritoDTO>
    findAllConDatos() {

        return favoritosRepository
                .findAll()
                .stream()
                .map(favoritoMapper::toDTO)
                .toList();
    }

    public FavoritoDTO
    findByIdConDatos(Long id) {
        Favorito favorito =
                favoritosRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new FavoritosNoEncontradoException(
                                        "Favorito no encontrado"
                                ));

        return favoritoMapper
                .toDTO(favorito);
    }
    public Favorito
    save(Favorito favorito) {

        favorito.setId(null);

        if (favoritosRepository
                .existsByUsuarioIdAndAnimeId(
                        favorito.getUsuarioId(),
                        favorito.getAnimeId()
                )) {

            throw new RuntimeException(
                    "El usuario ya tiene este anime en favoritos"
            );
        }

        return favoritosRepository
                .save(favorito);
    }
    public Favorito
    update(
            Long id,
            Favorito favorito) {

        Favorito favoritoExistente =
                favoritosRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new FavoritosNoEncontradoException(
                                        "Favorito no encontrado"
                                ));

        favoritoExistente.setUsuarioId(
                favorito.getUsuarioId()
        );

        favoritoExistente.setAnimeId(
                favorito.getAnimeId()
        );

        return favoritosRepository
                .save(favoritoExistente);
    }
    public void
    delete(Long id) {

        Favorito favorito =
                favoritosRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new FavoritosNoEncontradoException(
                                        "Favorito no encontrado"
                                ));

        favoritosRepository
                .delete(favorito);
    }
    public List<Favorito>
    buscarPorUsuario(
            Long usuarioId) {

        return favoritosRepository
                .findByUsuarioId(
                        usuarioId
                );
    }

    public List<Favorito>
    buscarPorAnime(
            Long animeId) {

        return favoritosRepository
                .findByAnimeId(
                        animeId
                );
    }

    public List<Favorito>
    findAll() {

        return favoritosRepository
                .findAll();
    }

    public Favorito
    findById(Long id) {

        return favoritosRepository
                .findById(id)
                .orElseThrow(() ->
                        new FavoritosNoEncontradoException(
                                "Favorito no encontrado"
                        ));
    }

    public Long
    conteoFavoritos(
            Long animeId) {

        return (long)
                favoritosRepository
                        .findByAnimeId(
                                animeId
                        )
                        .size();
    }
}