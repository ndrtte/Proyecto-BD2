package hn.unah.proyecto.entidades.oltp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Film")
public class Film {
    @Id
    @Column(name = "film_id")
    private Integer id;

    @Column(name = "title")
    private String titulo;

    @Column(name = "description")
    private String descripcion;

    @Column(name = "release_year")
    private String anioEstreno;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    private Language idioma;

    @ManyToOne
    @JoinColumn(name = "original_language_id", referencedColumnName = "language_id") //Opcional
    private Language idiomaOriginal;
    
    @Column(name = "rental_duration")
    private Integer duracionAlquiler;

    @Column(name = "rental_rate")
    private Integer tarifaAlquiler;
    
    @Column(name = "length")
    private Integer duracion;

    // @Column(name = "replacement_cost")
    // private Integer costoRemplazo;
    
    @Column(name = "rating")
    private String clasificacion;

    @Column(name = "special_features")
    private String caracteristicasEspeciales;
    
    @Column(name = "last_update")
    private Date ultimaActualizacion;
}


