package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.MigrationDataDTO;
import hn.unah.proyecto.dto.SourceTableDTO;

@Service
public class ETLService {

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

        String result;
        if(data.getMethod().equals("Tabla")) {
            result = migrarPorTabla(data);
        }
        else {
            result = migrarPorConsulta();
        }
        return result;
    }

    private String migrarPorTabla(MigrationDataDTO data) {
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

        return columnas;
    }


    private String migrarPorConsulta(){
        return "Migración por consulta no implementada";
    }

}
