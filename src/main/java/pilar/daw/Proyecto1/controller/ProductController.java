package pilar.daw.Proyecto1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import pilar.daw.Proyecto1.model.MarcaModel;
import pilar.daw.Proyecto1.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pilar.daw.Proyecto1.service.MarcaService;
import pilar.daw.Proyecto1.service.ProductService;
import pilar.daw.Proyecto1.util.ImageUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private MarcaService marcaService;
    @Operation(summary = "Listado de todos los productos", description = "Listado de productos", tags = {"productos"})
    @ApiResponse(responseCode = "200", description = "Lista de Productos")
    @GetMapping("/productos") //LISTAR PRODUCTOS
    public List<ProductModel> getProducts() {
        return this.productService.getProducts();
    }
    @Operation(summary = "Insertar un producto", description = "Inserta un producto", tags = {"productos"})
    @ApiResponse(responseCode = "200", description = "Producto añadido")
    @ApiResponse(responseCode = "400", description = "Datos no válidos")
    @PostMapping(value = "/producto", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductModel> createProduct(
            @RequestParam String nombre,
            @RequestParam Integer precio,
            @RequestParam Long idMarca,
            @RequestPart(name="imagen", required = false)MultipartFile imagen
    ) throws IOException {
        MarcaModel marca = new MarcaModel();
        Optional<MarcaModel> optionalMarcaModel = marcaService.getMarcaById(idMarca);
        if (((Optional<?>) optionalMarcaModel).isPresent()) {
            marca = optionalMarcaModel.get();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductModel createdProduct = productService.createProduct(new ProductModel(nombre, precio, marca), imagen);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    @Operation(summary = "Obtiene un producto por su Id", description = "Obtiene un producto dado su id", tags = {"productos"})
    @Parameter(name = "id", description = "ID del producto", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping(path = "/producto/{id}") //OBTENER PRODUCTO POR ID
    public ResponseEntity<ProductModel> getProductById(@PathVariable("id") Long id) {
        Optional<ProductModel> optionalProductModel = productService.getProductById(id);

        if (((Optional<?>) optionalProductModel).isPresent()) {
            optionalProductModel = productService.getProductById(id);
            return new ResponseEntity<>(optionalProductModel.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //return this.productService.getProductById(id); --> CÓDIGO ANTIGUO
    }
    @Operation(summary = "Actualizar un producto", description = "Actualizar un producto dado su id", tags = {"productos"})
    @Parameter(name = "id", description = "ID del producto", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "400", description = "Los datos del producto no son válidos")
    @PutMapping(value = "/producto/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) //MODIFICA UN PRODUCTO POR ID
    public ResponseEntity<ProductModel> updateProductById(
            @PathVariable("id") Long id,
            @RequestParam String nombre,
            @RequestParam Integer precio,
            @RequestParam Long idMarca,
            @RequestPart(name = "imagen", required = false) MultipartFile imagen) throws IOException {

        /* Es necesario elegir un id de marca existente*/
        MarcaModel marca = new MarcaModel();
        Optional<MarcaModel> optionalMarcaModel = marcaService.getMarcaById(id);
        if (((Optional<?>) optionalMarcaModel).isPresent()) {
            marca= optionalMarcaModel.get();
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        /* Tiene que existir la persona con ese id dado para poder actualizarla */
        Optional<ProductModel> optionalProductModel = productService.getProductById(id);

        if (((Optional<?>) optionalProductModel).isPresent()) {
            ProductModel existingProduct = optionalProductModel.get();
            existingProduct.setNombre(nombre);
            existingProduct.setPrecio(precio);
            existingProduct.setMarca(marca);
            existingProduct.setFecha_modificacion(LocalDateTime.now());
            existingProduct.setImagenBlob(ImageUtil.compressImage(imagen.getBytes()));

            ProductModel updatedProduct = productService.updateProduct(existingProduct, imagen);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Elimina un producto", description = "Elimina un producto dado su id", tags = {"productos"})
    @Parameter(name = "id", description = "ID del producto", required = true, example = "1")
    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping(path = "/producto/{id}") //BORRAR POR ID
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
        Optional<ProductModel> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Producto con id " + id + " eliminado correctamente", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Obtiene productos filtrando por nombre", description = "Obtiene una lista de productos", tags = {"productos"})
    @Parameter(name = "nombre", description = "Nombre del producto", required = true, example = "producto1")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/producto/nombre") //OBTENER PRODUCTO POR NOMBRE
    public ResponseEntity<List<ProductModel>> getProductByNombre(@RequestParam String nombre) {
        List<ProductModel> productos = productService.getByNombre(nombre);
        if (!productos.isEmpty()) {
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
     ////////////////////////
    @Operation(summary = "Ordenar por criterio", description = "Ordena los productos por el criterio introducido", tags = {"productos"})
    //@Parameter(name = "Criterio", description = "Nombre del producto", required = true, example = "producto1")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/producto/ordenar") //ORDENAR POR UN CRITERIO
    public ResponseEntity<List<ProductModel>> getByCriterio(@RequestParam String criterio) {
        List<ProductModel> productos = productService.getByCriterio(criterio);
        if (!productos.isEmpty()) {
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Muestra la imagen del producto", description = "Muestra la imagen dado el id del producto", tags = {"productos"})
    @Parameter(name = "id", description = "ID del producto", required = true, example = "2")
    @ApiResponse(responseCode = "200", description = "Imagen del producto")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping(value = "/producto/{id}/imagen", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> visualizarImagen(@PathVariable Long id) {
        byte[] imagen = productService.visualizarImagen(id);
        if (imagen != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imagen);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
