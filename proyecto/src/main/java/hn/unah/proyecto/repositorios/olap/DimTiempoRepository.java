package hn.unah.proyecto.repositorios.olap;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.dimTiempo;

public interface DimTiempoRepository extends JpaRepository<dimTiempo, Integer> {
    dimTiempo findByFecha(Date fecha);
}
