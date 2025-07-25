package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CiudadDTO;
import hn.unah.proyecto.entidades.olap.dimCiudad;
import hn.unah.proyecto.entidades.oltp.City;
import hn.unah.proyecto.repositorios.olap.DimCiudadRepository;
import hn.unah.proyecto.repositorios.oltp.CityRepository;

@Service
public class CiudadETLService {
    
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DimCiudadRepository dimCiudadRepository; 

    private List<City> extraerCiudadesOLTP() {
        return cityRepository.findAll();
    }

    public List<CiudadDTO> transformarCiudades(List<City> ciudadesOrigen){

        List<CiudadDTO> ciudadesDTO = new ArrayList<>();

        for (City ciudad : ciudadesOrigen) {
            CiudadDTO dto = new CiudadDTO();
            dto.setId(ciudad.getId());
            dto.setNombre(ciudad.getNombre());

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

    public void ejecutarETL() {
        List<City> origen = extraerCiudadesOLTP();
        List<CiudadDTO> transformadas = transformarCiudades(origen);
        cargarCiudadesOLAP(transformadas);
    }

    public List<dimCiudad> getAllCiudades() {
        return dimCiudadRepository.findAll();
    }
 }