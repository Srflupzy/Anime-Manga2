package episodio.cl.episodio_service.service;


import episodio.cl.episodio_service.model.Episodio;
import episodio.cl.episodio_service.repository.EpisodioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class EpisodioService {

    private final EpisodioRepository episodioRepository;

    public List<Episodio> findAll(){
        return episodioRepository.findAll();
    }

    public Episodio findById(Long id){
        return episodioRepository.findById(id).orElseThrow();
    }

    public Episodio save(Episodio episodio){
        episodio.setId(null);

        if (episodioRepository
                .existsByTituloIgnoreCaseAndTemporadaId(
                        episodio.getTitulo().trim(),
                        episodio.getTemporadaId())) {

            throw new RuntimeException(
                    "Ese episodio ya existe en la temporada"
            );
        }


        return episodioRepository.save(episodio);
    }

    public Episodio update(Long id, Episodio episodio){
        Episodio episodioExistente = episodioRepository.findById(id).orElseThrow();

        episodioExistente.setTitulo(episodio.getTitulo());
        episodioExistente.setDuracion(episodio.getDuracion());
        episodioExistente.setTemporadaId(episodio.getTemporadaId());

        return episodioRepository.save(episodioExistente);
    }

    public void delete(Long id){
        episodioRepository.deleteById(id);
    }

    public List<Episodio>
    buscarPorTitulo(
            String titulo){

        return episodioRepository
                .findByTituloContainingIgnoreCase(
                        titulo
                );
    }

    public List<Episodio>
    buscarPorTemporada(
            Long temporadaId){

        return episodioRepository
                .findByTemporadaId(
                        temporadaId
                );
    }

    public Episodio
    episodioMasLargo(){

        return episodioRepository
                .findTopByOrderByDuracionDesc()
                .orElseThrow();
    }
}
