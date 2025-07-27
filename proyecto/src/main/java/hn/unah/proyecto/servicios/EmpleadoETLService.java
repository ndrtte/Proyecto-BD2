package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

// import hn.unah.proyecto.dto.CategoriaDTO;
import hn.unah.proyecto.dto.EmpleadoDTO;
// import hn.unah.proyecto.entidades.olap.DimCategoria;
import hn.unah.proyecto.entidades.olap.DimEmpleado;
import hn.unah.proyecto.entidades.olap.DimTienda;
import hn.unah.proyecto.repositorios.olap.DimEmpleadoRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;
// import hn.unah.proyecto.repositorios.oltp.StaffRepository;
import hn.unah.proyecto.util.IncrementalETLHelper;

@Service
public class EmpleadoETLService {

    @Autowired
    private DimTiendaRepository tiendaRepository;

    // @Autowired
    // private StaffRepository staffRepository;

    @Autowired
    private DimEmpleadoRepository dimEmpleadoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerEmpleadosOLTP(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);

        return registros;
    }

    public List<EmpleadoDTO> transformarEmpleadoTabla(List<Map<String, Object>> empleadosOrigen) {
        List<EmpleadoDTO> empleadosTransformados = new ArrayList<>();

        for (Map<String, Object> empleado : empleadosOrigen) {
            EmpleadoDTO dto = new EmpleadoDTO();

            Integer id = ((Number) empleado.get("STAFF_ID")).intValue();
            String nombre = (String) empleado.get("FIRST_NAME");
            String apellido = (String) empleado.get("LAST_NAME");
            Integer idTienda = ((Number) empleado.get("STORE_ID")).intValue();

            dto.setIdEmpleado(id);
            dto.setNombre(nombre + " " + apellido);
            dto.setIdTienda(idTienda);

            empleadosTransformados.add(dto);
        }

        return empleadosTransformados;
    }

    public List<EmpleadoDTO> transformarEmpleadoConsulta(List<Map<String, Object>> empleadosOrigen) {
        List<EmpleadoDTO> empleadosTransformados = new ArrayList<>();

        for (Map<String, Object> empleado : empleadosOrigen) {
            EmpleadoDTO dto = new EmpleadoDTO();

            dto.setIdEmpleado(((Number) empleado.get("idEmpleado")).intValue());
            dto.setNombre((String) empleado.get("nombre"));
            dto.setIdTienda(((Number) empleado.get("idTienda")).intValue());

            empleadosTransformados.add(dto);
        }

        return empleadosTransformados;
    }

    private void cargarEmpleadosOLAP(List<EmpleadoDTO> empleadosTransformados) {

        List<DimEmpleado> empleados = new ArrayList<>();

        for (EmpleadoDTO dto : empleadosTransformados) {

            DimEmpleado empleado = dimEmpleadoRepository.findById(dto.getIdEmpleado()).orElse(null);

            if (empleado == null) {
                empleado = new DimEmpleado();
            }

            DimTienda tienda = tiendaRepository.findById(dto.getIdTienda()).orElse(null);

            empleado.setIdEmpleado(dto.getIdEmpleado());
            empleado.setNombre(dto.getNombre());
            empleado.setTienda(tienda);

            empleados.add(empleado);
        }

        dimEmpleadoRepository.saveAll(empleados);
    }

    public void ejecutarETL(String sqlQuery, String metodo) {
        List<Map<String, Object>> empleadosOrigen = extraerEmpleadosOLTP(sqlQuery);
        List<EmpleadoDTO> empleadosTransformados;

        if (metodo.equalsIgnoreCase("Table")) {
            empleadosTransformados = transformarEmpleadoTabla(empleadosOrigen);
        } else {
            empleadosTransformados = transformarEmpleadoConsulta(empleadosOrigen);
        }

        cargarEmpleadosOLAP(empleadosTransformados);
    }

    public List<DimEmpleado> getAllEmpleados() {
        return dimEmpleadoRepository.findAll();
    }

    /* 
    public void sincronizarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerEmpleadosOLTP(sqlQuery);
        List<EmpleadoDTO> empleadosDTO = transformarEmpleadoTabla(origen);
        List<DimEmpleado> existentes = dimEmpleadoRepository.findAll();

        IncrementalETLHelper.sincronizar(
            empleadosDTO,
            existentes,
            dto -> {
                DimTienda tienda = tiendaRepository.findById(dto.getIdTienda()).orElse(null);
                return new DimEmpleado(dto.getIdEmpleado(), dto.getNombre(), tienda);
            },       
            DimEmpleado::getIdEmpleado,
            entidad -> new EmpleadoDTO(entidad.getIdEmpleado(), entidad.getNombre(), entidad.getTienda().getIdTienda()),
            lista -> dimEmpleadoRepository.saveAll(lista),
            lista -> dimEmpleadoRepository.deleteAll(lista)
        );
    }    */
}