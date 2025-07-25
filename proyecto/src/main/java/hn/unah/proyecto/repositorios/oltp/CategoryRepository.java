package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;
import hn.unah.proyecto.entidades.oltp.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
