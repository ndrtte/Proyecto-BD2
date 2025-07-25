package hn.unah.proyecto.entidades.olap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_pelicula")
public class dimPelicula {
    @Id
    @Column(name = "id_pelicula")
    private Integer idPelicula;

    @Column(name = "titulo")
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private dimCategoria categoria;

    // @JoinColumn(name = "idioma")
    // private String idioma;

    // @JoinColumn(name = "rango_duracion")
    // private String rango_duracion;

    @Column(name = "audiencia")
    private String audiencia;
}
