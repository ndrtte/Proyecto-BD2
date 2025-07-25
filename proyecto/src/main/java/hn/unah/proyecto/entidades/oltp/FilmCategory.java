package hn.unah.proyecto.entidades.oltp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// @IdClass(FilmCategoryId.class)
@Table(name = "Film_Category")
public class FilmCategory {

    @EmbeddedId
    private FilmCategoryId id;
    
    // @ManyToOne
    // @JoinColumn(name = "film_id")
    // private Film pelicula;

    // @Id
    // @ManyToOne
    // @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    // private Category categoria;

    @ManyToOne
    @MapsId("filmId")
    private Film film;

    @ManyToOne
    @MapsId("categoryId")
    private Category category;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}
