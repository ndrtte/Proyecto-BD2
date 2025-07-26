package hn.unah.proyecto.repositorios.olap;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.DimTiempo;

public interface DimTiempoRepository extends JpaRepository<DimTiempo, Integer> {
    DimTiempo findByFecha(Date fecha);
}
