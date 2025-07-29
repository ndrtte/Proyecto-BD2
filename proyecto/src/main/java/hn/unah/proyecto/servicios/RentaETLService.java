package hn.unah.proyecto.servicios;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.RentaDTO;
import hn.unah.proyecto.entidades.olap.DimRenta;
import hn.unah.proyecto.repositorios.olap.DimRentaRepository;

@Service
public class RentaETLService {

    @Autowired
    private DimRentaRepository dimRentaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerRentasOLTP(String sqlQuery) {
        List<Map<String, Object>> registrosOLTP = jdbcTemplate.queryForList(sqlQuery);
        List<Map<String, Object>> registrosOLAP = jdbcTemplate.queryForList("SELECT ID_RENTA FROM TBL_RENTA");
        List<Map<String,Object>> registros = new ArrayList<>();

        for (Map<String,Object> filaOLTP: registrosOLTP) {
            Integer idOLTP = ((Number) filaOLTP.get("RENTAL_ID")).intValue();
            boolean existeEnOlap = false;
            
            for (Map<String,Object> filaOLAP : registrosOLAP) {
                Integer idOLAP = ((Number) filaOLAP.get("ID_RENTA")).intValue();

                if (idOLTP.equals(idOLAP)){
                    existeEnOlap = true;
                    break;
                }
            }
            if(!existeEnOlap){
                    registros.add(filaOLTP);
            }
        }

        return registros;
    }

    private Date convertirFecha(Object valor) {
        if (valor instanceof Timestamp) {
            return new Date(((Timestamp) valor).getTime());
        } else if (valor instanceof Date) {
            return (Date) valor;
        } else {
            return null;
        }
    }

    public List<RentaDTO> transformarRentas(List<Map<String, Object>> rentasOrigen) {

        List<RentaDTO> rentasDTO = new ArrayList<>();

        for (Map<String, Object> renta : rentasOrigen) {
            RentaDTO dtoRenta = new RentaDTO();
            Integer id = ((Number) renta.get("RENTAL_ID")).intValue();
            Date fechaRenta = convertirFecha(renta.get("RENTAL_DATE"));
            Date fechaDev = convertirFecha(renta.get("RETURN_DATE"));

            dtoRenta.setIdRenta(id);
            dtoRenta.setFechaRenta(fechaRenta);
            dtoRenta.setFechaDevolucion(fechaDev);
            rentasDTO.add(dtoRenta);
        }
        return rentasDTO;
    }

    private void cargarRentasOLAP(List<RentaDTO> rentasDTO) {

        List<DimRenta> rentasDestino = new ArrayList<>();

        for (RentaDTO dto : rentasDTO) {
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
}
