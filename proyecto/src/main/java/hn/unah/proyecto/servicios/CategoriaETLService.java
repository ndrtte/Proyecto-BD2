package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CategoriaDTO;
import hn.unah.proyecto.entidades.olap.DimCategoria;
import hn.unah.proyecto.repositorios.olap.DimCategoriaRepository;

@Service
public class CategoriaETLService {

    @Autowired
    private DimCategoriaRepository dimCategoriaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerCategoriasOLTP(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery + " WHERE NOT EXISTS ( SELECT 1 FROM TBL_CATEGORIA c WHERE c.ID_CATEGORIA = CATEGORY_ID) order by CATEGORY_ID");
        return registros;
    }

    public List<CategoriaDTO> transformarCategorias(List<Map<String, Object>> categoriasOrigen) {
        List<CategoriaDTO> categoriasDTO = new ArrayList<>();

        for (Map<String, Object> fila : categoriasOrigen) {
            Integer id = ((Number) fila.get("CATEGORY_ID")).intValue();
            String nombre = fila.get("NAME").toString();

            CategoriaDTO dto = new CategoriaDTO(id, nombre);
            categoriasDTO.add(dto);
        }

        return categoriasDTO;
    }

    private void cargarCategoriasOLAP(List<CategoriaDTO> categoriasDTO) {
        List<DimCategoria> categorias = new ArrayList<>();

        for (CategoriaDTO dto : categoriasDTO) {
            DimCategoria entidad = new DimCategoria();
            entidad.setIdCategoria(dto.getId());
            entidad.setNombreCategoria(dto.getNombre());
            categorias.add(entidad);
        }

        dimCategoriaRepository.saveAll(categorias);
    }

    public void ejecutarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerCategoriasOLTP(sqlQuery);
        List<CategoriaDTO> transformadas = transformarCategorias(origen);
        cargarCategoriasOLAP(transformadas);
    }

    public List<DimCategoria> getAllDimCategorias() {
        return dimCategoriaRepository.findAll();
    }

    /*
     * public void sincronizarETL(String sqlQuery) {
     * List<Map<String, Object>> origen = extraerCategoriasOLTP(sqlQuery);
     * List<CategoriaDTO> categoriasDTO = transformarCategorias(origen);
     * List<DimCategoria> existentes = dimCategoriaRepository.findAll();
     * 
     * IncrementalETLHelper.sincronizar(
     * categoriasDTO,
     * existentes,
     * dto -> new DimCategoria(dto.getId(), dto.getNombre()),
     * DimCategoria::getIdCategoria,
     * entidad -> new CategoriaDTO(entidad.getIdCategoria(),
     * entidad.getNombreCategoria()),
     * lista -> dimCategoriaRepository.saveAll(lista),
     * lista -> dimCategoriaRepository.deleteAll(lista)
     * );
     * }
     */

}
