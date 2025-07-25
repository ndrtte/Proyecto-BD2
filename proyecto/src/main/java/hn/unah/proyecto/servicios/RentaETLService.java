package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.RentaDTO;
import hn.unah.proyecto.entidades.olap.dimRenta;
import hn.unah.proyecto.entidades.oltp.Rental;
import hn.unah.proyecto.repositorios.olap.DimRentaRepository;
import hn.unah.proyecto.repositorios.oltp.RentalRepository;

@Service
public class RentaETLService {
    
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private DimRentaRepository dimRentaRepository;

    private List<Rental> extraerRentasOLTP(){
        return rentalRepository.findAll();
    }

    public List<RentaDTO> transformarRentas(List<Rental> rentasOrigen) {

        List<RentaDTO> rentasDTO = new ArrayList<>();

        for (Rental renta : rentasOrigen) {
            RentaDTO dtoRenta = new RentaDTO();
            dtoRenta.setIdRenta(renta.getId());
            dtoRenta.setFechaRenta(renta.getFechaRenta());
            dtoRenta.setFechaDevolucion(renta.getFechaDevolucion());

            rentasDTO.add(dtoRenta);
        }        
        return rentasDTO;
    } 

    private void cargarRentasOLAP(List<RentaDTO> rentasDTO) {

        List<dimRenta> rentasDestino = new ArrayList<>();

        for (RentaDTO dto : rentasDTO){
            dimRenta entidad = new dimRenta();
            entidad.setIdRenta(dto.getIdRenta());
            entidad.setFechaRenta(dto.getFechaRenta());
            entidad.setFechaDevolucion(dto.getFechaDevolucion());
            
            rentasDestino.add(entidad);
        }

        dimRentaRepository.saveAll(rentasDestino);
    }

    public void ejecutarETL() {
        List<Rental> origen = extraerRentasOLTP();
        List<RentaDTO> transformadas = transformarRentas(origen);
        cargarRentasOLAP(transformadas);
    }

    public List<dimRenta> getAllCiudades() {
        return dimRentaRepository.findAll();
    }    
}
