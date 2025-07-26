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
            
            if(data.getDestinationTable().equalsIgnoreCase("tbl_categoria")) {
                
            }else if (data.getDestinationTable().equalsIgnoreCase("tbl_ciudad")) {

            } else if (data.getDestinationTable().equalsIgnoreCase("tbl_empleado")) {

            } else if (data.getDestinationTable().equalsIgnoreCase("tbl_pelicula")) {

            } else if (data.getDestinationTable().equalsIgnoreCase("tbl_renta")) {

            } else if (data.getDestinationTable().equalsIgnoreCase("tbl_tiempo")) {

            } else if (data.getDestinationTable().equalsIgnoreCase("tbl_tienda")) {

            }else {
                //Si la tabla destino no es ninguna de las anteriores, mando mensaje al front
            }
        }
        else {
            registros = migrarPorConsulta(data);
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
