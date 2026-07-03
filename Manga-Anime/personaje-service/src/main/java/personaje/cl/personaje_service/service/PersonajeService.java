package personaje.cl.personaje_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personaje.cl.personaje_service.client.AnimeFeign;
import personaje.cl.personaje_service.dto.AnimeDTO;
import personaje.cl.personaje_service.dto.PersonajeDTO;
import personaje.cl.personaje_service.model.Personaje;
import personaje.cl.personaje_service.repository.PersonajeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PersonajeService {
    private final PersonajeRepository personajeRepository;
    private final AnimeFeign animeFeign;

    public List<Personaje> findAll(){
        return personajeRepository.findAll();
    }

    public Personaje findById(Long id){
        return personajeRepository.findById(id).orElseThrow();
    }

    public Personaje save(Personaje personaje){
        personaje.setId(null);

        if (personajeRepository
                .existsByNombreIgnoreCaseAndAnimeId(
                        personaje.getNombre().trim(),
                        personaje.getAnimeId())) {

            throw new RuntimeException(
                    "Ese personaje ya existe en el anime"
            );
        }
        AnimeDTO anime =
                animeFeign.pedirAnime(
                        personaje.getAnimeId()
                );
        if (anime == null) {

            throw new RuntimeException(
                    "El anime no existe"
            );
        }
        return personajeRepository.save(personaje);
    }

    public Personaje update (Long id, Personaje personaje){
        Personaje personajeExistente = personajeRepository.findById(id).orElseThrow();

        personajeExistente.setNombre(personaje.getNombre());
        personajeExistente.setRol(personaje.getRol());
        personajeExistente.setAnimeId(personaje.getAnimeId());

        return personajeRepository.save(personajeExistente);
    }

    public void delete(Long id){
        personajeRepository.deleteById(id);
    }

    public List<Personaje>
    buscarPorNombre(
            String nombre){

        return personajeRepository
                .findByNombreContainingIgnoreCase(
                        nombre
                );
    }
    public List<PersonajeDTO>
    findAllConAnime() {
        List<Personaje> personajes =
                personajeRepository.findAll();
        return personajes.stream()
                .map(p -> {
                    String animeNombre;
                    try {
                        animeNombre = animeFeign.pedirAnime(p.getAnimeId()).getTitulo();
                    } catch (Exception e) {

                        animeNombre = null;
                    }
                    return new PersonajeDTO(
                            p.getId(),
                            p.getNombre(),
                            p.getRol(),
                            animeNombre
                    );
                })
                .toList();
    }

    public PersonajeDTO
    findByIdConAnime(Long id) {

        Personaje personaje =
                personajeRepository.findById(id).orElseThrow(() -> new RuntimeException("Personaje no encontrado"));
        String animeNombre;
        try {
            animeNombre =
                    animeFeign
                            .pedirAnime(personaje.getAnimeId()).getTitulo();
        } catch (Exception e) {
            animeNombre = null;
        }
        return new PersonajeDTO(
                personaje.getId(),
                personaje.getNombre(),
                personaje.getRol(),
                animeNombre
        );
    }
}
