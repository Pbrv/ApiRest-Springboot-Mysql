package pilar.daw.Proyecto1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data //crea de forma transparente todos los métodos getters y setters
@Entity //proporciona la entidad que será mapeada a una tabla de BD
@Table(name = "producto")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre", nullable = false, length = 50)
    private String nombre;
    @Column(name="precio", nullable = false)
    private Integer precio;
    @ManyToOne
    @JoinColumn (name = "idMarca")
    @JsonBackReference
    //private Long marca; //SEGUNDA TABLA
    private MarcaModel marca;
    @Column(name="fecha_creacion")
    private LocalDateTime fecha_creacion = LocalDateTime.now();
    @Column(name="fecha_modificacion")
    private LocalDateTime fecha_modificacion = LocalDateTime.now();
    //IMAGENES
    @Column(name="imagen", nullable = false, length = 100)
    private String imagen;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="imagenBlob", columnDefinition = "longBlob", nullable = true)
    private byte[] imagenBlob;

    //CONSTRUCTORES
    public ProductModel() {
    }
    public ProductModel(String nombre, Integer precio, MarcaModel marca) {
        this.nombre = nombre;
        this.precio = precio;
        this.marca = marca;
    }

    public ProductModel(String nombre, Integer precio, Long idMarca) {
    }

    //GETTERS Y SETTERS
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return this.nombre;
    }
    public Integer getPrecio() {
        return this.precio;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }
    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
    public LocalDateTime getFecha_modificacion() {
        return fecha_modificacion;
    }
    public void setFecha_modificacion(LocalDateTime fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }
    public void setImagen(String originalFilename) {
        this.imagen = originalFilename;
    }
    public void setImagenBlob(byte[] data) {
        this.imagenBlob = data;
    }
    public byte[] getImagenBlob() {
        return imagenBlob;
    }
    public void setMarca(MarcaModel marca) {
        this.marca = marca;
    }
    public MarcaModel getMarca() {
        return this.marca;
    }
    @Override
    public String toString() {
        return "Ejemplo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edad=" + precio +
                '}';
    }
}
