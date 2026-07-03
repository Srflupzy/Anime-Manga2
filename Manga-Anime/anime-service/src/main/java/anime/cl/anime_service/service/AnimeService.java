package anime.cl.anime_service.service;

import anime.cl.anime_service.client.EstudioFeing;
import anime.cl.anime_service.client.GeneroFeign;
import anime.cl.anime_service.dto.AnimeDTO;
import anime.cl.anime_service.exception.AnimeDuplicadoException;
import anime.cl.anime_service.exception.AnimeNoEncontradoException;
import anime.cl.anime_service.mapper.AnimeMapper;
import anime.cl.anime_service.model.Anime;
import anime.cl.anime_service.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeMapper animeMapper;
    private final EstudioFeing estudioClient;
    private final GeneroFeign generoFeign;

    public List<AnimeDTO>
    findAll() {
        return animeRepository.findAll().stream().map(animeMapper::toDTO).toList();
    }

    public AnimeDTO
    findById(Long id) {
        Anime anime =
                animeRepository.findById(id).orElseThrow(() -> new AnimeNoEncontradoException("Anime no encontrado"));
        return animeMapper.toDTO(anime);
    }
    public Anime
    save(Anime anime) {

        anime.setId(null);

        if (animeRepository.existsByTituloIgnoreCase(anime.getTitulo().trim())) {
            throw new AnimeDuplicadoException(
                    anime.getTitulo()
            );
        }
        try {
            estudioClient.obtenerEstudio(anime.getEstudio());
        } catch (Exception e) {
            throw new RuntimeException("El estudio no existe");
        }
        try {
            generoFeign.obtenerGenero(anime.getGeneroId());
        } catch (Exception e) {
            throw new RuntimeException("El género no existe");
        }
        return animeRepository
                .save(anime);
    }
    public Anime
    update(Long id, Anime anime) {
        Anime animeExistente = animeRepository.findById(id).orElseThrow(() -> new AnimeNoEncontradoException("Anime no encontrado"));
        try {
            estudioClient.obtenerEstudio(anime.getEstudio());
        } catch (Exception e) {
            throw new RuntimeException("El estudio no existe");
        }
        try {
            generoFeign.obtenerGenero(anime.getGeneroId());
        } catch (Exception e) {
            throw new RuntimeException("El género no existe");
        }
        animeExistente.setTitulo(anime.getTitulo());
        animeExistente.setDescripcion(anime.getDescripcion());
        animeExistente.setEstudio(anime.getEstudio());
        animeExistente.setTemporadas(anime.getTemporadas());
        animeExistente.setGeneroId(anime.getGeneroId());
        return animeRepository.save(animeExistente);
    }

    public List<AnimeDTO>
    buscarPorTitulo(
            String titulo) {
        return animeRepository.findByTituloContainingIgnoreCase(titulo).stream().map(animeMapper::toDTO).toList();
    }

    public void
    delete(Long id) {
        Anime anime =
                animeRepository.findById(id).orElseThrow(() -> new AnimeNoEncontradoException("Anime no encontrado"));animeRepository.delete(anime);
    }
}