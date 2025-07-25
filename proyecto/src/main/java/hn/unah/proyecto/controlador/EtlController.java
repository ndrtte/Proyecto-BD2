package hn.unah.proyecto.controlador;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.proyecto.dto.MigrationDataDTO;
import hn.unah.proyecto.dto.SourceTableDTO;
import hn.unah.proyecto.servicios.MenuETLService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/etl")
public class EtlController {

    @Autowired
    private MenuETLService menuETLService;

    @PostMapping("/obtener/columnas")
    public List<String> postMethodName(@RequestBody SourceTableDTO sourceTable)
    {
        return menuETLService.obtenerColumnas(sourceTable);
    }
    
    @PostMapping("/migrar/datos")
    public String migracionDatos(@RequestBody MigrationDataDTO data) {
        
        return "";
    }
    
    

}
