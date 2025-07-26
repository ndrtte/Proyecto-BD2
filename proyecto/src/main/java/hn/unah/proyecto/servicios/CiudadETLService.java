package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CiudadDTO;
import hn.unah.proyecto.entidades.olap.dimCiudad;
import hn.unah.proyecto.repositorios.olap.DimCiudadRepository;
import hn.unah.proyecto.repositorios.oltp.CityRepository;

@Service
public class CiudadETLService {
    
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DimCiudadRepository dimCiudadRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerCiudadesOLTP(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);
        return registros;
    }

    public List<CiudadDTO> transformarCiudades(List<Map<String, Object>> ciudadesOrigen){
        List<CiudadDTO> ciudadesDTO = new ArrayList<>();

        for ( Map<String, Object> fila : ciudadesOrigen) {
            Integer id = ((Number) fila.get("CITY_ID")).intValue();
            String nombre = fila.get("CITY").toString();
            
            CiudadDTO dto = new CiudadDTO(id, nombre);
            ciudadesDTO.add(dto);
        }   
        
        return ciudadesDTO;
    } 

    private void cargarCiudadesOLAP(List<CiudadDTO> ciudadesDTO) {

        List<dimCiudad> ciudadesDestino = new ArrayList<>();

        for (CiudadDTO dto : ciudadesDTO){
            dimCiudad entidad = new dimCiudad();
            entidad.setIdCiudad((dto.getId()));
            entidad.setNombreCiudad(dto.getNombre());

            ciudadesDestino.add(entidad);
        }

        dimCiudadRepository.saveAll(ciudadesDestino);
    }

    public void ejecutarETL(String sqlQuery) {
         List<Map<String, Object>> origen = extraerCiudadesOLTP(sqlQuery);
        List<CiudadDTO> transformadas = transformarCiudades(origen);
        cargarCiudadesOLAP(transformadas);
    }

    public List<dimCiudad> getAllCiudades() {
        return dimCiudadRepository.findAll();
    }
 }