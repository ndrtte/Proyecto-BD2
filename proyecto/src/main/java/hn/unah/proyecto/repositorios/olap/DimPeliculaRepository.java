package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.dimPelicula;

public interface DimPeliculaRepository  extends JpaRepository<dimPelicula, Integer> {
    
}
