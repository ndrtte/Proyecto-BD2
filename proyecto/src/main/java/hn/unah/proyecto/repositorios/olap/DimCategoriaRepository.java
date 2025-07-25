package hn.unah.proyecto.repositorios.olap;

import org.springframework.data.jpa.repository.JpaRepository;
import hn.unah.proyecto.entidades.olap.dimCategoria;

public interface DimCategoriaRepository extends JpaRepository<dimCategoria, Integer> {
}
