package hn.unah.proyecto.entidades.oltp;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class FilmCategoryId implements Serializable {

    private Integer filmId;
    private Integer categoryId;
}


