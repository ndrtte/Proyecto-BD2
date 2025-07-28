package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CiudadDTO;
import hn.unah.proyecto.dto.RentaDTO;
import hn.unah.proyecto.dto.TiempoDTO;
import hn.unah.proyecto.entidades.olap.DimCiudad;
import hn.unah.proyecto.entidades.olap.DimRenta;
import hn.unah.proyecto.entidades.olap.DimTiempo;
import hn.unah.proyecto.entidades.oltp.Payment;
import hn.unah.proyecto.entidades.oltp.Rental;
import hn.unah.proyecto.repositorios.olap.DimTiempoRepository;
import hn.unah.proyecto.repositorios.oltp.PaymentRepository;
import hn.unah.proyecto.repositorios.oltp.RentalRepository;
import hn.unah.proyecto.util.IncrementalETLHelper;
import jakarta.transaction.Transactional;

@Service
public class TiempoETLService {
    
    // @Autowired
    // private RentalRepository rentalRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DimTiempoRepository dimTiempoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Date> extraerFechasRenta() {
        List<Payment> pagos = paymentRepository.findAll();
        
        List<Date> fechas = new ArrayList<>();

        for (Payment pago : pagos) {
            Date fecha = pago.getFechaPago();
            if (fecha != null) {
                fechas.add(fecha);
            }
        }
        return fechas;
    }

    private List<Map<String, Object>> extraerFechasPagosOLTP(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);
        return registros;
    }

    // public List<DimTiempo> transformarFechas(List<Date> fechas) {
    //     Set<LocalDate> fechasUnicas = new HashSet<>();
    //     for(Date date : fechas) {
    //         if(date != null) {
    //             LocalDate localDate = date.toInstant()
    //                                         .atZone(ZoneId.systemDefault())
    //                                         .toLocalDate();
    //             fechasUnicas.add(localDate);
    //         }
    //     }
    //     List<DimTiempo> tiempoTransformado = new ArrayList<>();
    //     for (LocalDate fecha : fechasUnicas) {
    //         DimTiempo tiempo = new DimTiempo();
    //         tiempo.setIdTiempo(Integer.parseInt(fecha.toString().replace("-", ""))); 
    //         tiempo.setFecha(java.sql.Date.valueOf(fecha));
    //         tiempo.setDiaSemana(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es"))); 
    //         tiempo.setMes(fecha.getMonthValue());
    //         tiempo.setAnio(fecha.getYear());
    //         tiempo.setTrimestre((fecha.getMonthValue() - 1) / 3 + 1);
    //         tiempoTransformado.add(tiempo);
    //     }
    //     return tiempoTransformado;    
    // }

    /* Luego lo podemos agregar en utils */
    private Date convertirFecha(Object valor) {
        if (valor instanceof Timestamp) {
            return new Date(((Timestamp) valor).getTime());
        } else if (valor instanceof Date) {
            return (Date) valor;
        } else {
            return null;
        }
    }

    public List<TiempoDTO> transformarFechasTabla(List<Map<String, Object>> fechasOrigen) {
       
        Set<LocalDate> fechasUnicas = new HashSet<>();

        for (Map<String, Object> fila : fechasOrigen) {
            Date fecha = convertirFecha(fila.get("PAYMENT_DATE"));
            if (fecha != null) {
                LocalDate localDate = fecha.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();
                fechasUnicas.add(localDate);
            }
        }

        List<TiempoDTO> fechasDTO = new ArrayList<>();

        for (LocalDate fecha : fechasUnicas) {
            TiempoDTO dto = new TiempoDTO();
            dto.setFecha(java.sql.Date.valueOf(fecha));
            dto.setDiaSemana(fecha.getDayOfWeek().getValue());
            dto.setMes(fecha.getMonthValue());
            dto.setAnio(fecha.getYear());
            dto.setTrimestre((fecha.getMonthValue() - 1) / 3 + 1);

            fechasDTO.add(dto);
        }

        return fechasDTO;
    }

    // private void cargarDimTiempo(List<DimTiempo> tiempos) {
    //     dimTiempoRepository.saveAll(tiempos);
    // }
    private void cargarFechasOLAP(List<TiempoDTO> fechasDTO) {
        
        List<DimTiempo> tiempoTransformado = new ArrayList<>();

        for (TiempoDTO dto : fechasDTO) {
            DimTiempo tiempo = new DimTiempo();

            tiempo.setFecha(dto.getFecha());
            tiempo.setDiaSemana(dto.getDiaSemana()); 
            tiempo.setMes(dto.getMes());
            tiempo.setAnio(dto.getAnio());
            tiempo.setTrimestre((dto.getMes() - 1) / 3 + 1);

            tiempoTransformado.add(tiempo);
        }
        System.out.println("Registros a insertar: " + tiempoTransformado.size());
        dimTiempoRepository.saveAll(tiempoTransformado);
    }

    public void ejecutarETL(String sqlQuery) {
        List<Map<String, Object>> origen = extraerFechasPagosOLTP(sqlQuery);
        List<TiempoDTO> transformadas = transformarFechasTabla(origen);
        cargarFechasOLAP(transformadas);
    }

    public List<DimTiempo> getAllDimTiempo() {
        return dimTiempoRepository.findAll();
    }

    // public void sincronizarETL(String sqlQuery) {
    //     // // Este cambio lo genero copilot
    //     List<Date> fechas = extraerFechasRenta();
    //     List<DimTiempo> tiemposTransformados = transformarFechas(fechas);
    //     List<TiempoDTO> fechasDTO = new ArrayList<>();
    //     for (DimTiempo tiempo : tiemposTransformados) {
    //         fechasDTO.add(new TiempoDTO(
    //             tiempo.getIdTiempo(),
    //             tiempo.getFecha(),
    //             tiempo.getDiaSemana(),
    //             tiempo.getMes(),
    //             tiempo.getAnio(),
    //             tiempo.getTrimestre()
    //         ));
    //     }
    //     //Logica del uso del metodo sincronizar() de todos los services
    //     List<DimTiempo> existentes = dimTiempoRepository.findAll();
        
    //     IncrementalETLHelper.sincronizar(
    //         fechasDTO,
    //         existentes,
    //         dto -> new DimTiempo(dto.getIdTiempo(), dto.getFecha(), dto.getDiaSemana(), dto.getMes(), dto.getAnio(), dto.getTrimestre()),
    //         DimTiempo::getIdTiempo,
    //         entidad -> new TiempoDTO(entidad.getIdTiempo(), entidad.getFecha(), entidad.getDiaSemana(), entidad.getMes(), entidad.getAnio(), entidad.getTrimestre()),
    //         lista -> dimTiempoRepository.saveAll(lista),
    //         lista -> dimTiempoRepository.deleteAll(lista)
    //     );
    // }  
}


