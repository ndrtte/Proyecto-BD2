package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.DimPelicula;

public interface DimPeliculaRepository  extends JpaRepository<DimPelicula, Integer> {
    
}
