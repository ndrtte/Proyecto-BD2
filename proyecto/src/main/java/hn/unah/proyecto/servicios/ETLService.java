package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.MigrationDataDTO;

@Service
public class ETLService {

    private String tablaDestino = "";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String consulta;

    @Autowired
    private CategoriaETLService categoriaETLService;

    @Autowired
    private CiudadETLService ciudadETLService;

    @Autowired
    private EmpleadoETLService empleadoETLService;

    public List<String> obtenerColumnas(String sourceTable) {
        String tablaOrigen = sourceTable.toUpperCase();

        List<String> columnas = new ArrayList<>();
        String sql = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ?";
        columnas = jdbcTemplate.queryForList(sql, String.class, tablaOrigen);

        return columnas;
    }

    public String migracionDatos(MigrationDataDTO data) {

        tablaDestino = data.getDestinationTable().toUpperCase();
        String sqlQuery;
        if (data.getMethod().equalsIgnoreCase("Table")) {
            sqlQuery = consultaPorTabla(data);

        } else {
            sqlQuery = data.getSourceTable().toUpperCase();
        }

        if (tablaDestino.equalsIgnoreCase("tbl_categoria")) {
            categoriaETLService.ejecutarETL(sqlQuery);
        } else if (tablaDestino.equalsIgnoreCase("tbl_ciudad")) {
            ciudadETLService.ejecutarETL(sqlQuery);
        } else if (tablaDestino.equalsIgnoreCase("tbl_empleado")) {
            
        } else if (tablaDestino.equalsIgnoreCase("tbl_pelicula")) {

        } else if (tablaDestino.equalsIgnoreCase("tbl_renta")) {

        } else if (tablaDestino.equalsIgnoreCase("tbl_tiempo")) {

        } else if (tablaDestino.equalsIgnoreCase("tbl_tienda")) {

        } else {
            // Si la tabla destino no es ninguna de las anteriores, mando mensaje al front
        }

        return "";
    }

    private String consultaPorTabla(MigrationDataDTO data) {
        String columnas = "";
        int tamanio = data.getListColumn().size();
        int i = 0;

        for (String columna : data.getListColumn()) {
            columnas = columnas + columna.toUpperCase();
            i++;
            if (i < tamanio) {
                columnas += ", ";
            }
        }

        return "SELECT " + columnas.toUpperCase() + " FROM " + data.getSourceTable().toUpperCase();
    }
}
