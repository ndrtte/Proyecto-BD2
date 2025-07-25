package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.City;

public interface CityRepository extends JpaRepository<City, Integer> {

    
}
