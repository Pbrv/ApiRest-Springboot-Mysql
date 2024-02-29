package pilar.daw.Proyecto1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pilar.daw.Proyecto1.exceptions.BadRequestProduct;
import pilar.daw.Proyecto1.model.MarcaModel;
import pilar.daw.Proyecto1.model.ProductModel;
import pilar.daw.Proyecto1.repository.IProductRepository;
import pilar.daw.Proyecto1.util.ImageUtil;

import pilar.daw.Proyecto1.exceptions.ExceptionProduct;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;
    public List<ProductModel> getProducts() {
        return productRepository.findAll();
    }

    public ProductModel createProduct(ProductModel productModel, MultipartFile file) throws ExceptionProduct, IOException {
        if (productModel.getNombre() == null || productModel.getNombre().isEmpty()) {
            throw new BadRequestProduct("No se ha introducido el nombre del producto");
        }
        if (productModel.getPrecio() == null) {
            throw new BadRequestProduct("No se ha introducido el precio");
        }

        Long idMarca = productModel.getMarca().getId();

        if (idMarca == null || idMarca <= 0 || idMarca > 3)
            throw new BadRequestProduct("Debe introducirse un valor de marca entre 1 y 3");
        String marca;
        if ( idMarca == 1)
            marca = "rayban";
        else if ( idMarca == 2)
            marca = "vogue";
        else
            marca = "oakley";

        ProductModel productSave = new ProductModel(productModel.getNombre(), productModel.getPrecio(), new MarcaModel(idMarca, marca));

        if (!file.isEmpty()) {
            productSave.setImagen(file.getOriginalFilename());
            productSave.setImagenBlob(ImageUtil.compressImage(file.getBytes())); //Almacena en BD el archivo binario

            /*ALMACENAR IMAGEN EN DISCO
            * COMENTAR PARA DESPLIEGUE DOCKER*/
            Path dir = Paths.get("src//main//resources//static//img");
            String rutaAbsoluta = dir.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = file.getBytes();
                Path ruta = Paths.get(rutaAbsoluta + "//" + file.getOriginalFilename());
                Files.write(ruta, bytesImg);
            } catch (IOException e) {
                throw new ExceptionProduct("Error de escritura");
            }
        } else {
            throw new BadRequestProduct("No se ha insertado la imagen");
        }
        return productRepository.save(productSave); //productSave
    }
    public Optional<ProductModel> getProductById(Long id) {
        return productRepository.findById(id);
    }



    public ProductModel updateProduct(ProductModel productModel, MultipartFile file) throws IOException {
        if (productModel.getNombre() == null || productModel.getNombre().isEmpty()) {
            throw new BadRequestProduct("Debe introducirse un nombre");
        }
        if (productModel.getPrecio() == null || productModel.getPrecio() <= 0) {
            throw new BadRequestProduct("Debe introducirse una edad real");
        }

        Long idMarca = productModel.getMarca().getId();

        if (idMarca == null || idMarca <= 0 || idMarca > 3)
            throw new BadRequestProduct("Debe introducirse un valor de marca entre 1 y 3");

        String marca;
        if ( idMarca == 1)
            marca = "rayban";
        else if ( idMarca == 2)
            marca = "vogue";
        else
            marca = "oakley";

        ProductModel productsave = new ProductModel(productModel.getNombre(), productModel.getPrecio(), new MarcaModel(idMarca, marca));

        if (!file.isEmpty()) {
            productModel.setImagen(file.getOriginalFilename());
            productModel.setImagenBlob(ImageUtil.compressImage(file.getBytes()));

            //COMENTAR ESTO PARA DOCKER
            Path dir = Paths.get("src//main//resources//static//img");
            String rutaAbsoluta = dir.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = file.getBytes();
                Path ruta = Paths.get(rutaAbsoluta + "//" + file.getOriginalFilename());
                Files.write(ruta, bytesImg);
            } catch (IOException e) {
                throw new ExceptionProduct("Error de escritura");
            }
        } else {
            throw new BadRequestProduct("Debe introducirse la imagen");
        }
        return productRepository.save(productModel); // productSave

        /*Optional<ProductModel> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductModel existingProduct = optionalProduct.get();
            if (request.getNombre() != null) {
                existingProduct.setNombre(request.getNombre());
            }
            if (request.getPrecio() != null) {
                existingProduct.setPrecio(request.getPrecio());
            }
            // Puedes agregar aquí más campos para actualizar

            existingProduct.setFecha_modificacion(LocalDateTime.now());
            productRepository.save(existingProduct);
            return existingProduct;
        } else {
            // Manejar el caso en que el producto no se encuentre
            //throw new ProductNotFoundException("No se encontró un producto con el ID: " + id);
            return null;
        }*/

        //CÓDIGO ANTERIOR
        //ProductModel product = IProductRepository.findById(id).get();

        //product.setNombre(request.getNombre());
        //product.setPrecio(request.getPrecio());
        //product.setImagen_blob(request.getImagen_blob()); /*NO SE*/
        //IProductRepository.save(product);

        //return product;
    }
    public Boolean deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (ExceptionProduct e) {
            return false;
        }
    }

    public List<ProductModel> getByNombre(String nombre) {
        return productRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<ProductModel> getByCriterio(String criterio) {
        if (!Objects.equals(criterio, "precio")) {
            throw new ExceptionProduct("El criterio de ordenación no es válido, insertar precio");
        }
        return productRepository.findAllByOrderByPrecio();
    }
    public byte[] visualizarImagen(Long id) {
        ProductModel productModel = productRepository.findById(id).orElse(null);
        return productModel != null ? ImageUtil.decompressImage(productModel.getImagenBlob()) : null;
    }
}
