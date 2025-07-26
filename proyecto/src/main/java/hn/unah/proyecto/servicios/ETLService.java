package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.MigrationDataDTO;
import hn.unah.proyecto.dto.SourceTableDTO;

@Service
public class ETLService {

    private List<Map<String, Object>> registros = new ArrayList<>();
    private String tablaDestino = "";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> obtenerColumnas(SourceTableDTO sourceTable) {
        String tablaOrigen = sourceTable.getSourceTable().toUpperCase();
        String metodo = sourceTable.getMethod();
        List<String> columnas = new ArrayList<>();
        String sql;

        if (metodo.equalsIgnoreCase("Tabla")) {
            sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ?";
            columnas = jdbcTemplate.queryForList(sql, String.class, tablaOrigen);
        }
        return columnas;
    }

    public String migracionDatos(MigrationDataDTO data) {

        tablaDestino = data.getDestinationTable().toUpperCase();
        if(data.getMethod().equals("Tabla")) {
            registros = migrarPorTabla(data);
        }
        else {
            registros = migrarPorConsulta(data);
        }

        switch (tablaDestino) {
                case "tbl_categoria":
                    break;
                case "tbl_ciudad":
                    break;
                case "tbl_empleado":
                    break;
                case "tbl_renta":
                    break;
                case "tbl_tienda":
                    break;
                case "tbl_tiempo":
                    break;
                case "tbl_pelicula":
                    break;
                default:
                    break;
            }

        return "";
    }

    private List<Map<String, Object>> migrarPorTabla(MigrationDataDTO data) {
        String columnas = "";
        int tamanio = data.getListColumn().size();
        int i = 0;

        for (String columna : data.getListColumn()) {
            columnas = columnas + columna.toUpperCase();
            i++;
            if(i < tamanio){
                columnas += ", ";
            }
        }

        String sql = "SELECT " + columnas.toUpperCase() + " FROM " + data.getSourceTable().toUpperCase();


        return jdbcTemplate.queryForList(sql);
    }


    private List<Map<String, Object>> migrarPorConsulta(MigrationDataDTO data) {
        return jdbcTemplate.queryForList(data.getSourceTable().toUpperCase());
    }

}
