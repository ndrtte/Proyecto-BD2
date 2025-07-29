package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.TiendaDTO;
import hn.unah.proyecto.entidades.olap.DimCiudad;
import hn.unah.proyecto.entidades.olap.DimTienda;
import hn.unah.proyecto.repositorios.olap.DimCiudadRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;

@Service
public class TiendaETLService {

    @Autowired
    private DimTiendaRepository dimTiendaRepository;

    @Autowired
    private DimCiudadRepository dimCiudadRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    private List<Map<String, Object>> extraerTiendasOLTP(String sqlQuery) {
        List<Map<String, Object>> registrosOLTP = jdbcTemplate.queryForList(sqlQuery);
        List<Map<String, Object>> registrosOLAP = jdbcTemplate.queryForList("SELECT ID_TIENDA FROM TBL_TIENDA");
        List<Map<String, Object>> registros = new ArrayList<>();

        for (Map<String, Object> filaOLTP : registrosOLTP) {
            Integer idOLTP = ((Number) filaOLTP.get("STORE_ID")).intValue();
            boolean existeEnOlap = false;

            for (Map<String, Object> filaOLAP : registrosOLAP) {
                Integer idOLAP = ((Number) filaOLAP.get("ID_TIENDA")).intValue();

                if (idOLTP.equals(idOLAP)) {
                    existeEnOlap = true;
                    break;
                }

                if (!existeEnOlap) {
                    registros.add(filaOLTP);
                }

            }
        }

        return registros;
    }

    private List<TiendaDTO> transformarTiendas(List<Map<String, Object>> tiendasOrigen) {
        List<TiendaDTO> tiendasDTO = new ArrayList<>();
        int i = 1;

        for (Map<String, Object> row : tiendasOrigen) {
            TiendaDTO dto = new TiendaDTO();

            Integer idTienda = ((Number) row.get("STORE_ID")).intValue();
            Integer idCiudad = ((Number) row.get("CITY_ID")).intValue();

            dto.setIdTienda(idTienda);
            dto.setIdCiudad(idCiudad);
            dto.setNombreTienda("Tienda " + i);
            i++;

            tiendasDTO.add(dto);
        }

        return tiendasDTO;
    }

    private void cargarTiendasOLAP(List<TiendaDTO> tiendasDTO) {

        List<DimTienda> tiendasDestino = new ArrayList<>();

        for (TiendaDTO dto : tiendasDTO) {

            DimCiudad ciudad = dimCiudadRepository.findById(dto.getIdCiudad()).orElse(null);

            DimTienda entidad = new DimTienda();
            entidad.setIdTienda(dto.getIdTienda());
            entidad.setNombreTienda(dto.getNombreTienda());
            entidad.setCiudad(ciudad);

            tiendasDestino.add(entidad);
        }

        dimTiendaRepository.saveAll(tiendasDestino);
    }

    public void ejecutarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerTiendasOLTP(sqlQuery);
        List<TiendaDTO> transformadas = transformarTiendas(origen);
        cargarTiendasOLAP(transformadas);
    }

    public List<DimTienda> getAllTiendas() {
        return dimTiendaRepository.findAll();
    }

}
