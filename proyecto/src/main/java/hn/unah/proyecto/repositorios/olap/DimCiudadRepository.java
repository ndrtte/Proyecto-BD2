package hn.unah.proyecto.repositorios.olap;
import hn.unah.proyecto.entidades.olap.dimCiudad;


import org.springframework.data.jpa.repository.JpaRepository;

public interface DimCiudadRepository extends JpaRepository<dimCiudad, Integer> {
    
}
