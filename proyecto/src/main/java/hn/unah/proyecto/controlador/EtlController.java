package hn.unah.proyecto.controlador;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.proyecto.dto.MigrationDataDTO;
import hn.unah.proyecto.dto.SourceTableDTO;
import hn.unah.proyecto.servicios.CategoriaETLService;
import hn.unah.proyecto.servicios.ETLService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/etl")
@CrossOrigin(origins = "*")
public class EtlController {

    @Autowired
    private ETLService etlService;


    @PostMapping("/obtener/columnas")
    public List<String> postMethodName(@RequestBody SourceTableDTO sourceTable){
        return etlService.obtenerColumnas(sourceTable);
    }
    
    @PostMapping("/migrar/datos")
    public String migracionDatos(@RequestBody MigrationDataDTO data) {
        return etlService.migracionDatos(data);
    }
    
    @Autowired
    private CategoriaETLService categoriaETLService;

}
