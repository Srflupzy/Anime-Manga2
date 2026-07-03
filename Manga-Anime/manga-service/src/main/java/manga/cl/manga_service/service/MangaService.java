package manga.cl.manga_service.service;

import lombok.RequiredArgsConstructor;
import manga.cl.manga_service.client.GeneroFeign;
import manga.cl.manga_service.dto.GeneroDTO;
import manga.cl.manga_service.dto.MangaDTO;
import manga.cl.manga_service.model.Manga;
import manga.cl.manga_service.repository.MangaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MangaService {

    private final MangaRepository mangaRepository;
    private final GeneroFeign generoFeign;

    public List<Manga> findAll(){
        return mangaRepository.findAll();
    }

    public Manga findById(Long id){
        return mangaRepository.findById(id).orElseThrow();
    }

    public Manga save (Manga manga){
        manga.setId(null);

        if (mangaRepository
                .existsByTituloIgnoreCase(
                        manga.getTitulo().trim())) {

            throw new RuntimeException(
                    "Ya existe un manga con ese título"
            );
        }

        GeneroDTO genero =
                generoFeign.pedirGenero(
                        manga.getGeneroId()
                );

        if (genero == null) {

            throw new RuntimeException(
                    "El género no existe"
            );
        }



        return mangaRepository.save(manga);
    }

    public Manga update(Long id, Manga manga){
        Manga mangaExistente = mangaRepository.findById(id).orElseThrow();

        mangaExistente.setTitulo(manga.getTitulo());
        mangaExistente.setAutor(manga.getAutor());
        mangaExistente.setTomos(manga.getTomos());
        mangaExistente.setGeneroId(manga.getGeneroId());

        return mangaRepository.save(mangaExistente);
    }

    public void delete(Long id){
        mangaRepository.deleteById(id);
    }


    public List<Manga>
    buscarPorTitulo(
            String titulo){

        return mangaRepository
                .findByTituloContainingIgnoreCase(
                        titulo
                );
    }
    public List<MangaDTO>
    findAllConGenero() {

        List<Manga> mangas =
                mangaRepository.findAll();

        return mangas.stream()
                .map(m -> {
                    String generoNombre;
                    try {
                        generoNombre =
                                generoFeign.pedirGenero(m.getGeneroId()).getNombre();
                    } catch (Exception e) {
                        generoNombre = null;
                    }
                    return new MangaDTO(
                            m.getId(),
                            m.getTitulo(),
                            m.getAutor(),
                            m.getTomos(),
                            generoNombre
                    );
                })
                .toList();
    }
    public MangaDTO
    findByIdConGenero(Long id) {
        Manga manga =
                mangaRepository.findById(id).orElseThrow(() -> new RuntimeException("Manga no encontrado"));
        String generoNombre;
        try {
            generoNombre =
                    generoFeign
                            .pedirGenero(manga.getGeneroId()).getNombre();
        } catch (Exception e) {
            generoNombre = null;
        }
        return new MangaDTO(
                manga.getId(),
                manga.getTitulo(),
                manga.getAutor(),
                manga.getTomos(),
                generoNombre
        );
    }
}
