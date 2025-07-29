package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.TiempoDTO;
import hn.unah.proyecto.entidades.olap.DimTiempo;
import hn.unah.proyecto.repositorios.olap.DimTiempoRepository;

@Service
public class TiempoETLService {

    @Autowired
    private DimTiempoRepository dimTiempoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerFechasPagosOLTP(String sqlQuery, String metodo) {
        List<Map<String,Object>> registros;

        if(metodo.equalsIgnoreCase("Table")){
            registros = jdbcTemplate.queryForList( sqlQuery + " WHERE NOT EXISTS (SELECT 1 FROM TBL_TIEMPO t WHERE t.fecha = TRUNC(payment.payment_date))");
        }else{
            registros = jdbcTemplate.queryForList( sqlQuery + " WHERE NOT EXISTS (SELECT 1 FROM TBL_TIEMPO t WHERE t.fecha = TRUNC(p.payment_date))");

        }
        
        return registros;
    }

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

    public List<TiempoDTO> transformarFechasConsulta(List<Map<String, Object>> fechasOrigen) {
        List<TiempoDTO> tiemposDTO = new ArrayList<>();

        for (Map<String, Object> fila : fechasOrigen) {
            TiempoDTO tiempoDTO = new TiempoDTO();

            Date fecha = ((Date) fila.get("PAYMENT_DATE"));
            Integer diaSemana = ((Number) fila.get("DIA_SEMANA")).intValue();
            Integer mes = ((Number) fila.get("MES")).intValue();
            Integer anio = ((Number) fila.get("ANIO")).intValue();
            Integer trimestre = ((Number) fila.get("TRIMESTRE")).intValue();

            tiempoDTO.setFecha(fecha);
            tiempoDTO.setDiaSemana(diaSemana);
            tiempoDTO.setMes(mes);
            tiempoDTO.setAnio(anio);
            tiempoDTO.setTrimestre(trimestre);

            tiemposDTO.add(tiempoDTO);
        }

        return tiemposDTO;
    }

    private void cargarFechasOLAPTabla(List<TiempoDTO> fechasDTO) {

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
        dimTiempoRepository.saveAll(tiempoTransformado);
    }

    private void cargarFechasOLAPConsulta(List<TiempoDTO> fechasDTO) {

        List<DimTiempo> tiempoTransformado = new ArrayList<>();

        for (TiempoDTO dto : fechasDTO) {
            DimTiempo tiempo = new DimTiempo();

            tiempo.setFecha(dto.getFecha());
            tiempo.setDiaSemana(dto.getDiaSemana());
            tiempo.setMes(dto.getMes());
            tiempo.setAnio(dto.getAnio());
            tiempo.setTrimestre(dto.getTrimestre());

            tiempoTransformado.add(tiempo);
        }
        dimTiempoRepository.saveAll(tiempoTransformado);
    }

    public void ejecutarETL(String sqlQuery, String metodo) {
        List<Map<String, Object>> origen = extraerFechasPagosOLTP(sqlQuery, metodo);
        List<TiempoDTO> transformadas;
        if (metodo.equalsIgnoreCase("Table")) {
            transformadas = transformarFechasTabla(origen);
            cargarFechasOLAPTabla(transformadas);
        } else {
            transformadas = transformarFechasConsulta(origen);
            cargarFechasOLAPConsulta(transformadas);
        }
    }

    public List<DimTiempo> getAllDimTiempo() {
        return dimTiempoRepository.findAll();
    }
}
