package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CiudadDTO;
import hn.unah.proyecto.entidades.olap.DimCiudad;
import hn.unah.proyecto.repositorios.olap.DimCiudadRepository;

@Service
public class CiudadETLService {

    @Autowired
    private DimCiudadRepository dimCiudadRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerCiudadesOLTP(String sqlQuery) {
        List<Map<String, Object>> registrosOLTP = jdbcTemplate.queryForList(sqlQuery);
        List<Map<String, Object>> registrosOLAP = jdbcTemplate.queryForList("SELECT ID_CIUDAD FROM TBL_CIUDAD");
        List<Map<String, Object>> registros = new ArrayList<>();

        for (Map<String, Object> filaOLTP : registrosOLTP) {
            Integer idOLTP = ((Number) filaOLTP.get("CITY_ID")).intValue();
            boolean existeEnOlap = false;

            for (Map<String, Object> filaOLAP : registrosOLAP) {
                Integer idOLAP = ((Number) filaOLAP.get("ID_CIUDAD")).intValue();

                if (idOLTP.equals(idOLAP)) {
                    existeEnOlap = true;
                    break;
                }

            }
            if (!existeEnOlap) {
                registros.add(filaOLTP);
            }
        }

        return registros;
    }

    public List<CiudadDTO> transformarCiudades(List<Map<String, Object>> ciudadesOrigen) {
        List<CiudadDTO> ciudadesDTO = new ArrayList<>();

        for (Map<String, Object> fila : ciudadesOrigen) {
            Integer id = ((Number) fila.get("CITY_ID")).intValue();
            String nombre = fila.get("CITY").toString();

            CiudadDTO dto = new CiudadDTO(id, nombre);
            ciudadesDTO.add(dto);
        }

        return ciudadesDTO;
    }

    private void cargarCiudadesOLAP(List<CiudadDTO> ciudadesDTO) {

        List<DimCiudad> ciudadesDestino = new ArrayList<>();

        for (CiudadDTO dto : ciudadesDTO) {
            DimCiudad entidad = new DimCiudad();
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

    public List<DimCiudad> getAllCiudades() {
        return dimCiudadRepository.findAll();
    }

}