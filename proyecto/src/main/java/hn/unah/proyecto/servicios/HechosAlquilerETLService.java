package hn.unah.proyecto.servicios;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.HechosDTO;
import hn.unah.proyecto.entidades.olap.Hechos;
import hn.unah.proyecto.entidades.oltp.Rental;
import hn.unah.proyecto.repositorios.olap.DimEmpleadoRepository;
import hn.unah.proyecto.repositorios.olap.DimPeliculaRepository;
import hn.unah.proyecto.repositorios.olap.DimRentaRepository;
import hn.unah.proyecto.repositorios.olap.DimTiempoRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;
import hn.unah.proyecto.repositorios.oltp.RentalRepository;

@Service
public class HechosAlquilerETLService {
    
    @Autowired
    private RentalRepository rentalRepository;

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
    

    private List<Map<String, Object>> extraerRentasOLTP(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);
        return registros;
    }

    private List<HechosDTO> transformarRentas(List<Map<String, Object>> hechos) {

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

            Rental renta = rentalRepository.findById(rentaId).get();

            //tiempo renta
            Duration duracion = Duration.between(renta.getFechaRenta().toInstant(), renta.getFechaDevolucion().toInstant());

            double horas = duracion.toHours();

            HechosDTO dto = new HechosDTO();

            dto.setIdRenta(rentaId);
            dto.setIdEmpleado(empleadoId);
            dto.setIdPelicula(peliculaId);
            dto.setIdTienda(tiendaId);
            dto.setIdTiempo(tiempoId);
            dto.setMontoPago(monto);
            dto.setCantidad(cantidad);

            String unidadTiempo;

            if (horas < 24) {
                dto.setTiempoRenta(horas);
                unidadTiempo = "Horas";
            } else {
                double dias = horas / 24.0;
                dto.setTiempoRenta(dias);
                unidadTiempo = "Días";
            }
            dto.setUnidadTiempo(unidadTiempo);

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
            hecho.setAudiencia(dto.getAudiencia());
            hecho.setCantidad(dto.getCantidad());
            hecho.setTiempoRenta(dto.getTiempoRenta());
            hecho.setUnidadTiempo(dto.getUnidadTiempo());

            hechos.add(hecho);
        }

       // hechosRepository.saveAll(hechos);
    }

    public void ejecutarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerRentasOLTP(sqlQuery);
        List<HechosDTO> transformadas = transformarRentas(origen);
        cargarHechosOLAP(transformadas);
    }
}
