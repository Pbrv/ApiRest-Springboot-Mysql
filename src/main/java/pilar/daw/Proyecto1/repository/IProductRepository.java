package pilar.daw.Proyecto1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pilar.daw.Proyecto1.model.ProductModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByNombreContainingIgnoreCase(String nombre);

    List<ProductModel> findAllByOrderByPrecio();
}
