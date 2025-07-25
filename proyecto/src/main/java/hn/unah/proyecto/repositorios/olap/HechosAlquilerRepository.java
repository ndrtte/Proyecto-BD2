package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.olap.Hechos;

public interface HechosAlquilerRepository extends JpaRepository<Hechos, Integer> {    
}
