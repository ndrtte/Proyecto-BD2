package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;
import hn.unah.proyecto.entidades.oltp.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

}
