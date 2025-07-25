package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Film;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    
}
