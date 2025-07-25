package hn.unah.proyecto.repositorios.oltp;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.FilmCategory;
import hn.unah.proyecto.entidades.oltp.FilmCategoryId;

public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId>{
	
    Optional<FilmCategory> findByFilmId(Integer filmId);
}

