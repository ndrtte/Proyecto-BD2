package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.entidades.olap.dimTiempo;
import hn.unah.proyecto.entidades.oltp.Rental;
import hn.unah.proyecto.repositorios.olap.DimTiempoRepository;
import hn.unah.proyecto.repositorios.oltp.RentalRepository;

@Service
public class TiempoETLService {
    
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private DimTiempoRepository dimTiempoRepository;

    private List<Date> extraerFechasRenta() {
        List<Rental> rentas = rentalRepository.findAll();
        
        List<Date> fechas = new ArrayList<>();

        for (Rental renta : rentas) {
            Date fecha = renta.getFechaRenta();
            if (fecha != null) {
                fechas.add(fecha);
            }
        }
        return fechas;
    }

    public List<dimTiempo> transformarFechas(List<Date> fechas) {
        Set<LocalDate> fechasUnicas = new HashSet<>();
        for(Date date : fechas) {
            if(date != null) {
                LocalDate localDate = date.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                fechasUnicas.add(localDate);
            }
        }

        List<dimTiempo> tiempoTransformado = new ArrayList<>();

        for (LocalDate fecha : fechasUnicas) {
            dimTiempo tiempo = new dimTiempo();

            tiempo.setIdTiempo(Integer.parseInt(fecha.toString().replace("-", ""))); 
            tiempo.setFecha(java.sql.Date.valueOf(fecha));
            tiempo.setDiaSemana(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es"))); 
            tiempo.setMes(fecha.getMonthValue());
            tiempo.setAnio(fecha.getYear());
            tiempo.setTrimestre((fecha.getMonthValue() - 1) / 3 + 1);

            tiempoTransformado.add(tiempo);
        }

        return tiempoTransformado;    
    }

    private void cargarDimTiempo(List<dimTiempo> tiempos) {
        dimTiempoRepository.saveAll(tiempos);
    }

    public void ejecutarETL() {
        List<Date> fechas = extraerFechasRenta();
        List<dimTiempo> tiempoTransformado = transformarFechas(fechas);
        cargarDimTiempo(tiempoTransformado);
    }

    public List<dimTiempo> getAllDimTiempo() {
        return dimTiempoRepository.findAll();
    }
}


