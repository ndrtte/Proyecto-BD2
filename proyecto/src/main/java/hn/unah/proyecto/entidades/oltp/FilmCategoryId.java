package hn.unah.proyecto.entidades.oltp;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FilmCategoryId implements Serializable {

    private Integer filmId;
    private Integer categoryId;
}


