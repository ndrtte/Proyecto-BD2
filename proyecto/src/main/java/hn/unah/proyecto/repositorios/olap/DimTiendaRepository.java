package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.DimTienda;


public interface DimTiendaRepository extends JpaRepository<DimTienda, Integer> {

    
}
