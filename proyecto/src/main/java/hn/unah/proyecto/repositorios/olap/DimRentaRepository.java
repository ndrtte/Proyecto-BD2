package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;
import hn.unah.proyecto.entidades.olap.dimRenta;

public interface DimRentaRepository extends JpaRepository<dimRenta, Integer> {
}
