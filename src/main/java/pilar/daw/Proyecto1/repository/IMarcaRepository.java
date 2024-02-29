package pilar.daw.Proyecto1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pilar.daw.Proyecto1.model.MarcaModel;

public interface IMarcaRepository extends JpaRepository <MarcaModel, Long> {

}
