package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;
import hn.unah.proyecto.entidades.olap.DimCategoria;

public interface DimCategoriaRepository extends JpaRepository<DimCategoria, Integer> {
}
