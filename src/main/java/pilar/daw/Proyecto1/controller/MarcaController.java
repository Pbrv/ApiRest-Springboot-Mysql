package pilar.daw.Proyecto1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pilar.daw.Proyecto1.model.MarcaModel;
import pilar.daw.Proyecto1.service.MarcaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class MarcaController {
    @Autowired
    private MarcaService marcaService;

    @Operation(summary = "Listado de las marcas", description = "Obtiene una lista de todas las marcas", tags = {"marcas"})
    @ApiResponse(responseCode = "200", description = "Lista de Marcas")
    @GetMapping("/marcas")
    public List<MarcaModel> getMarcas() {
        return marcaService.getMarcas();
    }

    @Operation(summary = "Obtiene la marca de un producto", description = "Obtiene la marca dado el id del producto", tags = {"marcas"})
    @Parameter(name = "id", description = "ID de la marca", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Marca encontrada")
    @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    @GetMapping("/marca/{id}")
    public ResponseEntity<MarcaModel> getMarcaById(@PathVariable Long id) {
        Optional<MarcaModel> optionalMarcaModel = marcaService.getMarcaById(id);

        if (((Optional<?>) optionalMarcaModel).isPresent()) {
            optionalMarcaModel = marcaService.getMarcaById(id);
            return new ResponseEntity<>(optionalMarcaModel.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
