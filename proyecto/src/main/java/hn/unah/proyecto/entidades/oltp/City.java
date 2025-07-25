package hn.unah.proyecto.entidades.oltp;

import java.util.Date;

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
@Table(name = "City")
public class City {
    
    @Id
    @Column(name = "city_id")
    private Integer id;

    @Column(name = "city")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country pais;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}
