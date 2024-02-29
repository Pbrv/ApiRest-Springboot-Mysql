package pilar.daw.Proyecto1.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "marca")
public class MarcaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca", nullable = false, length = 30)
    private String marca;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductModel> productos;

    public MarcaModel() {}

    public MarcaModel(Long id, String marca) {
        this.id = id;
        this.marca = marca;
    }

    public Long getId() {
        return this.id;
    }
}
