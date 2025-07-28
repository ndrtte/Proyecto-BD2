package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.HechosDTO;
import hn.unah.proyecto.entidades.olap.Hechos;
import hn.unah.proyecto.repositorios.olap.DimEmpleadoRepository;
import hn.unah.proyecto.repositorios.olap.DimPeliculaRepository;
import hn.unah.proyecto.repositorios.olap.DimRentaRepository;
import hn.unah.proyecto.repositorios.olap.DimTiempoRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;
import hn.unah.proyecto.repositorios.olap.HechosAlquilerRepository;

@Service
public class HechosAlquilerETLService {
    
    @Autowired
    private DimRentaRepository dimRentaRepository;

    @Autowired
    private DimTiempoRepository dimTiempoRepository;

    @Autowired
    private DimEmpleadoRepository dimEmpleadoRepository;

    @Autowired
    private DimPeliculaRepository dimPeliculaRepository;

    @Autowired
    private DimTiendaRepository dimTiendaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HechosAlquilerRepository hechosRepository;
    

    private List<Map<String, Object>> extraerHechosOLTP(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery + " WHERE NOT EXISTS ( SELECT 1 FROM tbl_hechos_renta h WHERE h.id_renta = r.rental_id AND h.id_empleado = r.staff_id AND h.id_pelicula = i.film_id AND h.id_tienda = i.store_id AND h.id_fecha = t.id_fecha )");
        return registros;
    }

    private List<HechosDTO> transformarHechos(List<Map<String, Object>> hechos) {

        List<HechosDTO> hechosDTO = new ArrayList<>();

        for (Map<String, Object> hecho : hechos) {

            Integer rentaId = ((Number) hecho.get("ID_RENTA")).intValue();
            Integer empleadoId = ((Number) hecho.get("ID_EMPLEADO")).intValue();
            Integer peliculaId = ((Number) hecho.get("ID_PELICULA")).intValue();
            Integer tiendaId = ((Number) hecho.get("ID_TIENDA")).intValue();
            Integer tiempoId = ((Number) hecho.get("ID_FECHA")).intValue();

            //metricas
            //ingresos
            Double monto = ((Number) hecho.get("INGRESOS")).doubleValue();

            //cantidad:
            Integer cantidad = 1;


            HechosDTO dto = new HechosDTO();

            dto.setIdRenta(rentaId);
            dto.setIdEmpleado(empleadoId);
            dto.setIdPelicula(peliculaId);
            dto.setIdTienda(tiendaId);
            dto.setIdTiempo(tiempoId);
            dto.setMontoPago(monto);
            dto.setCantidad(cantidad);

            hechosDTO.add(dto);
        }
        return hechosDTO;
    }

    private void cargarHechosOLAP (List<HechosDTO> hechosDTO) {

        List<Hechos> hechos = new ArrayList<>();

        for(HechosDTO dto : hechosDTO) {
            Hechos hecho = new Hechos();

            hecho.setRenta(dimRentaRepository.findById(dto.getIdRenta()).orElse(null));
            hecho.setEmpleado(dimEmpleadoRepository.findById(dto.getIdEmpleado()).orElse(null));
            hecho.setPelicula(dimPeliculaRepository.findById(dto.getIdPelicula()).orElse(null));
            hecho.setTienda(dimTiendaRepository.findById(dto.getIdTienda()).orElse(null));
            hecho.setTiempo(dimTiempoRepository.findById(dto.getIdTiempo()).orElse(null));

            hecho.setMontoPago(dto.getMontoPago());
            hecho.setCantidad(dto.getCantidad());
            hecho.setTiempoRenta(dto.getTiempoRenta());

            hechos.add(hecho);
        }

        hechosRepository.saveAll(hechos);
    }

    public void ejecutarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerHechosOLTP(sqlQuery);
        List<HechosDTO> transformadas = transformarHechos(origen);
        cargarHechosOLAP(transformadas);
    }
}
