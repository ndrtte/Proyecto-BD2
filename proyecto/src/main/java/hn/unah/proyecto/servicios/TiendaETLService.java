package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.TiendaDTO;
import hn.unah.proyecto.entidades.olap.dimCiudad;
import hn.unah.proyecto.entidades.olap.dimTienda;
import hn.unah.proyecto.entidades.oltp.City;
import hn.unah.proyecto.entidades.oltp.Store;
import hn.unah.proyecto.repositorios.olap.DimCiudadRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;
import hn.unah.proyecto.repositorios.oltp.CityRepository;
import hn.unah.proyecto.repositorios.oltp.StoreRepository;

@Service
public class TiendaETLService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private DimTiendaRepository dimTiendaRepository;

    @Autowired
    private DimCiudadRepository dimCiudadRepository;
    
    @Autowired
    private CityRepository cityRepository;

    private List<Store> extraerTiendasOLTP(){
        return storeRepository.findAll();
    }

    public List<TiendaDTO> transformarRentas(List<Store> tiendasOrigen) {

        List<TiendaDTO> tiendasDTO = new ArrayList<>();

        for (Store tienda : tiendasOrigen) {
            TiendaDTO dtoTienda = new TiendaDTO();

            City city = cityRepository.findById(tienda.getId()).orElse(null);
            Integer idCiudad = city.getId();

            dtoTienda.setIdTienda(tienda.getId());
            dtoTienda.setNombreTienda(dtoTienda.getNombreTienda());
            dtoTienda.setIdTienda(idCiudad);


            tiendasDTO.add(dtoTienda);
        }        
        return tiendasDTO;
    } 

    private void cargarTiendasOLAP(List<TiendaDTO> tiendasDTO) {

        List<dimTienda> tiendasDestino = new ArrayList<>();

        for (TiendaDTO dto : tiendasDTO){
        
            dimCiudad ciudad = dimCiudadRepository.findById(dto.getIdCiudad()).orElse(null);

            dimTienda entidad = new dimTienda();
            entidad.setIdTienda(dto.getIdTienda());
            entidad.setNombreTienda(dto.getNombreTienda());
            entidad.setCiudad(ciudad);
            
            tiendasDestino.add(entidad);
        }

        dimTiendaRepository.saveAll(tiendasDestino);
    }

    public void ejecutarETL() {
        List<Store> origen = extraerTiendasOLTP();
        List<TiendaDTO> transformadas = transformarRentas(origen);
        cargarTiendasOLAP(transformadas);
    }

    public List<dimTienda> getAllTiendas() {
        return dimTiendaRepository.findAll();
    }    
    
}
