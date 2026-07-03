package estudio.cl.estudio_service.service;


import estudio.cl.estudio_service.model.Estudio;
import estudio.cl.estudio_service.repository.EstudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudioService {

    private final EstudioRepository estudioRepository;

    public List<Estudio> findAll(){
        return estudioRepository.findAll();
    }

    public Estudio findById(Long id){
        return estudioRepository.findById(id).orElseThrow();
    }



    public Estudio save(Estudio estudio){
        estudio.setId(null);

        if (estudioRepository
                .existsByNombreIgnoreCase(
                        estudio.getNombre().trim())) {

            throw new RuntimeException(
                    "Ya existe un estudio con ese nombre"
            );
        }

        return estudioRepository.save(estudio);
    }

    public Estudio update(Long id, Estudio estudio){
        Estudio estudioExistente = estudioRepository.findById(id).orElseThrow();

        estudioExistente.setNombre(estudio.getNombre());
        estudioExistente.setPais(estudio.getPais());

        return estudioRepository.save(estudioExistente);
    }

    public void delete(Long id){
        estudioRepository.deleteById(id);
    }

    public Estudio
    buscarPorNombre(
            String nombre) {

        return estudioRepository
                .findByNombreIgnoreCase(
                        nombre
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Estudio no encontrado"
                        ));
    }

}
