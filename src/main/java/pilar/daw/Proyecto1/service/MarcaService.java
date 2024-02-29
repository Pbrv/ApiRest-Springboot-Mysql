package pilar.daw.Proyecto1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pilar.daw.Proyecto1.model.MarcaModel;
import pilar.daw.Proyecto1.repository.IMarcaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {
    @Autowired
    private IMarcaRepository marcaRepository;

    public List<MarcaModel> getMarcas() {
        return marcaRepository.findAll();
    }
    public Optional<MarcaModel> getMarcaById(Long id) {
        /*return Optional.ofNullable(marcaRepository.findById(id).orElseThrow(
                () -> new MarcaNotFoundException("la marca con id " + id + " no existe")
        ));*/
        return marcaRepository.findById(id);
    }
}
