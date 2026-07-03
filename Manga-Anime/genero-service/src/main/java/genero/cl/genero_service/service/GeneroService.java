package genero.cl.genero_service.service;

import genero.cl.genero_service.model.Genero;
import genero.cl.genero_service.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class GeneroService {

    private final GeneroRepository generoRepository;

    public List<Genero> findAll() {
        return generoRepository.findAll();
    }

    public Genero findById(Long id) {
        return generoRepository.findById(id).orElseThrow();
    }

    public Genero save(Genero genero) {

        genero.setId(null);

        if (generoRepository.existsByNombreIgnoreCase(genero.getNombre().trim())) {
            throw new RuntimeException("El genero ya existe");
        }

        return generoRepository.save(genero);
    }

    public Genero update(Long id, Genero genero) {
        Genero generoExistente = generoRepository.findById(id).orElseThrow();

        generoExistente.setNombre(genero.getNombre());
        generoExistente.setDescripcion(genero.getDescripcion());

        return generoRepository.save(generoExistente);
    }

    public void delete(Long id) {
        generoRepository.deleteById(id);
    }

    public List<Genero>
    buscarPorNombre(
            String nombre) {

        return generoRepository
                .findByNombreContainingIgnoreCase(
                        nombre
                );
    }
}
