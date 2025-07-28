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

    @Autowired
    private CategoriaETLService categoriaETLService;

    @Autowired
    private CiudadETLService ciudadETLService;

    @Autowired
    private EmpleadoETLService empleadoETLService;

    @Autowired
    private PeliculaETLService peliculaETLService;

    @Autowired
    private RentaETLService rentaETLService;

    @Autowired
    private TiempoETLService tiempoETLService;

    @Autowired
    private TiendaETLService tiendaETLService;

    @Autowired
    private HechosAlquilerETLService hechosETLService;

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
        String metodo = data.getMethod();
        if (metodo.equalsIgnoreCase("Table")) {
            sqlQuery = consultaPorTabla(data);

        } else {
            sqlQuery = data.getSourceTable();
        }

        if (tablaDestino.equalsIgnoreCase("tbl_categoria")) {
            categoriaETLService.ejecutarETL(sqlQuery);
        } else if (tablaDestino.equalsIgnoreCase("tbl_ciudad")) {
            ciudadETLService.ejecutarETL(sqlQuery);
        } else if (tablaDestino.equalsIgnoreCase("tbl_empleado")) {
            empleadoETLService.ejecutarETL(sqlQuery, metodo);
        } else if (tablaDestino.equalsIgnoreCase("tbl_pelicula")) {
            peliculaETLService.ejecutarETL(sqlQuery, metodo);
        } else if (tablaDestino.equalsIgnoreCase("tbl_renta")) {
            rentaETLService.ejecutarETL(sqlQuery);
        } else if (tablaDestino.equalsIgnoreCase("tbl_tiempo")) {
            tiempoETLService.ejecutarETL(sqlQuery, metodo);
        } else if (tablaDestino.equalsIgnoreCase("tbl_tienda")) {
            tiendaETLService.ejecutarETL(sqlQuery);
        } else if(tablaDestino.equalsIgnoreCase("tbl_hechos_renta")){
            hechosETLService.ejecutarETL(sqlQuery);
        }

        return "Migracion exitosa";
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
