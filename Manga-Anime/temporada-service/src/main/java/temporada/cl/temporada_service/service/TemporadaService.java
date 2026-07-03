package temporada.cl.temporada_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import temporada.cl.temporada_service.client.AnimeFeign;
import temporada.cl.temporada_service.dto.AnimeDTO;
import temporada.cl.temporada_service.dto.TemporadaDTO;
import temporada.cl.temporada_service.exception.TemporadaDuplicadaException;
import temporada.cl.temporada_service.model.Temporada;
import temporada.cl.temporada_service.repository.TemporadaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemporadaService {

    private final TemporadaRepository temporadaRepository;
    private final AnimeFeign animeFeign;

    public List<Temporada> findAll() {
        return temporadaRepository.findAll();
    }

    public Temporada findById(Long id) {
        return temporadaRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Temporada no encontrada"
                        ));
    }

    public Temporada save(Temporada temporada) {

        temporada.setId(null);

        if (temporadaRepository
                .existsByNumeroAndAnimeId(
                        temporada.getNumero(),
                        temporada.getAnimeId())) {

            throw new TemporadaDuplicadaException(
                    temporada.getNumero()
            );
        }

        AnimeDTO anime =
                animeFeign.buscarAnime(
                        temporada.getAnimeId()
                );

        if (anime == null) {
            throw new RuntimeException(
                    "El anime no existe"
            );
        }

        return temporadaRepository
                .save(temporada);
    }

    public Temporada update(
            Long id,
            Temporada temporada) {

        Temporada temporadaExistente =
                temporadaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Temporada no encontrada"
                                ));

        temporadaExistente
                .setNumero(
                        temporada.getNumero()
                );

        temporadaExistente
                .setAnimeId(
                        temporada.getAnimeId()
                );

        return temporadaRepository
                .save(temporadaExistente);
    }

    public void delete(Long id) {

        if (!temporadaRepository
                .existsById(id)) {

            throw new RuntimeException(
                    "Temporada no encontrada"
            );
        }

        temporadaRepository
                .deleteById(id);
    }

    public List<Temporada>
    buscarPorNumero(
            Integer numero) {

        return temporadaRepository
                .findByNumero(numero);
    }

    public List<Temporada>
    buscarPorAnime(
            Long animeId) {

        return temporadaRepository
                .findByAnimeId(animeId);
    }

    public Temporada
    buscarTemporadaEspecifica(
            Long animeId,
            Integer numero) {

        return temporadaRepository
                .findByAnimeIdAndNumero(
                        animeId,
                        numero
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Temporada no encontrada"
                        ));
    }

    public TemporadaDTO
    findByIdConAnime(Long id) {

        Temporada temporada =
                temporadaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Temporada no encontrada"
                                ));

        String animeNombre;

        try {

            animeNombre =
                    animeFeign
                            .buscarAnime(
                                    temporada.getAnimeId()
                            )
                            .getTitulo();

        } catch (Exception e) {

            animeNombre = null;
        }

        return new TemporadaDTO(
                temporada.getId(),
                temporada.getNumero(),
                animeNombre
        );
    }

    public List<TemporadaDTO>
    buscarPorAnimeConNombre(
            Long animeId) {

        List<Temporada> temporadas =
                temporadaRepository
                        .findByAnimeId(
                                animeId
                        );

        return temporadas.stream()
                .map(t -> {

                    String animeNombre;

                    try {

                        animeNombre =
                                animeFeign
                                        .buscarAnime(
                                                t.getAnimeId()
                                        )
                                        .getTitulo();

                    } catch (Exception e) {

                        animeNombre = null;
                    }

                    return new TemporadaDTO(
                            t.getId(),
                            t.getNumero(),
                            animeNombre
                    );

                })
                .toList();
    }

    public List<TemporadaDTO>
    findAllConNombre() {

        List<Temporada> temporadas =
                temporadaRepository.findAll();

        return temporadas.stream()
                .map(t -> {

                    String animeNombre;

                    try {

                        animeNombre =
                                animeFeign
                                        .buscarAnime(
                                                t.getAnimeId()
                                        )
                                        .getTitulo();

                    } catch (Exception e) {

                        animeNombre = null;
                    }

                    return new TemporadaDTO(
                            t.getId(),
                            t.getNumero(),
                            animeNombre
                    );

                })
                .toList();
    }
}