package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.EmpleadoDTO;
import hn.unah.proyecto.entidades.olap.dimEmpleado;
import hn.unah.proyecto.entidades.olap.dimTienda;
import hn.unah.proyecto.entidades.oltp.Staff;
import hn.unah.proyecto.repositorios.olap.DimEmpleadoRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;
import hn.unah.proyecto.repositorios.oltp.StaffRepository;

@Service
public class EmpleadoETLService {

    @Autowired
    private DimTiendaRepository tiendaRepository;

    @Autowired 
    private StaffRepository staffRepository;

    @Autowired
    private DimEmpleadoRepository dimEmpleadoRepository;

    private List<Staff> extraerEmpleadosOLTP() {
        return staffRepository.findAll();
    }

    public List<EmpleadoDTO> transformarEmpleado(List<Staff> empleadosOrigen) {

        List<EmpleadoDTO> empleadosTransformados = new ArrayList<>();
        
        for (Staff empleado : empleadosOrigen) {
            
            EmpleadoDTO dto = new EmpleadoDTO();

            dto.setIdEmpleado(empleado.getId());
            
            String nombreCompleto = empleado.getNombre() + " "+ empleado.getApellido();
            dto.setNombre(nombreCompleto);
            dto.setIdTienda(empleado.getTienda().getId());

            empleadosTransformados.add(dto);
        }
        return empleadosTransformados;
    }

    private void cargarEmpleadosOLAP(List<EmpleadoDTO> empleadosTransformados) {

        List<dimEmpleado> empleados = new ArrayList<>();
        
        for (EmpleadoDTO dto : empleadosTransformados) {

            dimEmpleado empleado = dimEmpleadoRepository.findById(dto.getIdEmpleado()).orElse(null);

            if (empleado == null) {
                empleado = new dimEmpleado();
            }

            dimTienda tienda = tiendaRepository.findById(dto.getIdTienda()).orElse(null);
            
            empleado.setIdEmpleado(dto.getIdEmpleado());
            empleado.setNombre(dto.getNombre());
            empleado.setTienda(tienda);

            empleados.add(empleado);
        }

        dimEmpleadoRepository.saveAll(empleados);        
    }

    public void ejecutarETL() {
        List<Staff> empleadosOrigen = extraerEmpleadosOLTP();
        List<EmpleadoDTO> empleadosTransformados = transformarEmpleado(empleadosOrigen);
        cargarEmpleadosOLAP(empleadosTransformados);
    }

    public List<dimEmpleado> getAllEmpleados() {
        return dimEmpleadoRepository.findAll();
    }
}