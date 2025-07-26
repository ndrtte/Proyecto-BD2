package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.DimEmpleado;

public interface DimEmpleadoRepository extends JpaRepository<DimEmpleado, Integer> {
    
}
