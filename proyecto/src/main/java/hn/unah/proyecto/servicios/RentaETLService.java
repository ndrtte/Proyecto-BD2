package hn.unah.proyecto.servicios;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

// import hn.unah.proyecto.dto.CategoriaDTO;
import hn.unah.proyecto.dto.RentaDTO;
// import hn.unah.proyecto.entidades.olap.DimCategoria;
import hn.unah.proyecto.entidades.olap.DimRenta;
// import hn.unah.proyecto.entidades.oltp.Rental;
import hn.unah.proyecto.repositorios.olap.DimRentaRepository;
// import hn.unah.proyecto.repositorios.oltp.RentalRepository;
import hn.unah.proyecto.util.IncrementalETLHelper;

@Service
public class RentaETLService {
    
    // @Autowired
    // private RentalRepository rentalRepository;

    @Autowired
    private DimRentaRepository dimRentaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>>  extraerRentasOLTP(String sqlQuery){
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);
        return registros;
    }

    public List<RentaDTO> transformarRentas(List<Map<String, Object>> rentasOrigen) {

        List<RentaDTO> rentasDTO = new ArrayList<>();

        for (Map<String, Object> renta : rentasOrigen) {
            RentaDTO dtoRenta = new RentaDTO();
            Integer id = ((Number) renta.get("RENTAL_ID")).intValue();
            Date fechaRenta = (Date) renta.get("RENTAL_DATE");
            Date fechaDev = (Date) renta.get("RETURN_DATE");


            dtoRenta.setIdRenta(id);
            dtoRenta.setFechaRenta(fechaRenta);
            dtoRenta.setFechaDevolucion(fechaDev);
            rentasDTO.add(dtoRenta);
        }
        return rentasDTO;
    } 

    private void cargarRentasOLAP(List<RentaDTO> rentasDTO) {

        List<DimRenta> rentasDestino = new ArrayList<>();

        for (RentaDTO dto : rentasDTO){
            DimRenta entidad = new DimRenta();
            entidad.setIdRenta(dto.getIdRenta());
            entidad.setFechaRenta(dto.getFechaRenta());
            entidad.setFechaDevolucion(dto.getFechaDevolucion());
            
            rentasDestino.add(entidad);
        }

        dimRentaRepository.saveAll(rentasDestino);
    }

    public void ejecutarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerRentasOLTP(sqlQuery);
        List<RentaDTO> transformadas = transformarRentas(origen);
        cargarRentasOLAP(transformadas);
    }

    public List<DimRenta> getAllCiudades() {
        return dimRentaRepository.findAll();
    }

    public void sincronizarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerRentasOLTP(sqlQuery);
        List<RentaDTO> rentasDTO = transformarRentas(origen);
        List<DimRenta> existentes = dimRentaRepository.findAll();

        IncrementalETLHelper.sincronizar(
            rentasDTO,
            existentes,
            dto -> new DimRenta(dto.getIdRenta(), dto.getFechaRenta(), dto.getFechaDevolucion()),          
            DimRenta::getIdRenta,
            entidad -> new RentaDTO(entidad.getIdRenta(), entidad.getFechaRenta(), entidad.getFechaDevolucion()),
            lista -> dimRentaRepository.saveAll(lista),
            lista -> dimRentaRepository.deleteAll(lista)
        );
    }    
}
