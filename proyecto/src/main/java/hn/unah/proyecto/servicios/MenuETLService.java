package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.SourceTableDTO;

@Service
public class MenuETLService {

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

}
